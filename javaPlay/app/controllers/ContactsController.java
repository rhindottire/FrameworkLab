package controllers;

import models.Contact;
import play.data.Form;
import play.i18n.Messages;
import repositories.ContactsRepository;
import services.ContactsService;
import views.html.Contacts.*;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Controller;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.i18n.MessagesApi;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

public class ContactsController extends Controller {

    private final ContactsRepository contactsRepository;
    private final ContactsService contactService;
    private final HttpExecutionContext ec;
    private final FormFactory formFactory;
    private final MessagesApi messagesApi;

    @Inject
    public ContactsController(
            ContactsRepository contactsRepository,
            ContactsService contactService,
            HttpExecutionContext ec,
            FormFactory formFactory,
            MessagesApi messagesApi
    ) {
        this.contactsRepository = contactsRepository;
        this.contactService = contactService;
        this.ec = ec;
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
    }

    public CompletionStage<Result> index() {
        try {
            return contactService.getAllContacts()
                    .thenApply(contacts -> ok(views.html.Contacts.index.render(contacts)))
                    .exceptionally(ex -> {
                        return internalServerError("Error: " + ex.getCause().getMessage());
                    });
        } catch (Exception e) {
            return CompletableFuture.completedFuture(internalServerError("System error occurred"));
        }
    }

    public Result createContact(Http.Request request) {
        try {
            Form<Contact> contactForm = formFactory.form(Contact.class);
            Messages messages = messagesApi.preferred(request);
            return ok(views.html.Contacts.create.render(contactForm, request, messages));
        } catch (Exception e) {
            return internalServerError("Failed to load add contact page");
        }
    }
    public CompletionStage<Result> saveContact(Http.Request request) {
        try {
            Form<Contact> contactForm = formFactory.form(Contact.class).bindFromRequest(request);
            if (contactForm.hasErrors()) {
                return CompletableFuture.completedFuture(
                        badRequest(views.html.Contacts.create.render(contactForm, request, messagesApi.preferred(request)))
                );
            }
            Contact contact = contactForm.get();
            return contactService.addContact(contact)
                    .thenApply(savedContact ->
                            redirect(routes.ContactsController.index()).flashing("success", "Contact have been saved!")
                    )
                    .exceptionally(ex -> {
                        return internalServerError("Error: " + ex.getCause().getMessage());
                    });
        } catch (Exception e) {
            return CompletableFuture.completedFuture(internalServerError("System Error: " + e.getMessage()));
        }
    }

    public Result editContact(int id, Http.Request request) {
        try {
            return ok(edit.render(
                    formFactory.form(Contact.class).fill(contactService.getContactById(id)),
                    request,
                    messagesApi.preferred(request).asScala()
            ));
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }
    public CompletionStage<Result> updateContact(int id, Http.Request request) {
        try {
            Form<Contact> contactForm = formFactory.form(Contact.class).bindFromRequest(request);
            if (contactForm.hasErrors()) {
                System.out.println("Form errors: " + contactForm.errors());
                return CompletableFuture.completedFuture(
                        badRequest(views.html.Contacts.edit.render(
                                contactForm,
                                request,
                                messagesApi.preferred(request).asScala()
                        ))
                );
            }
            Contact formContact = contactForm.get();
            Contact updatedContact = new Contact(
                    id,
                    formContact.getName(),
                    formContact.getEmail(),
                    formContact.getPhone()
            );
            return contactService.updateContact(updatedContact)
                    .thenApply(result ->
                            redirect(routes.ContactsController.index())
                                    .flashing("Success", "Contacts updated successfully!")
                    )
                    .exceptionally(ex -> {
                        return internalServerError(ex.getCause().getMessage());
                    });
        } catch (Exception e) {
            return CompletableFuture.completedFuture(internalServerError("Error: " + e.getMessage()));
        }
    }

    public CompletionStage<Result> showContact(int id) {
        return contactsRepository.getContactById(id)
                .thenApplyAsync(contact -> {
                    if (contact != null) {
                        return ok(show.render(contact));
                    } else {
                        return notFound("Contact not found");
                    }
                }, ec.current())
                .exceptionally(throwable -> {
                    if (throwable.getCause() instanceof Exception) {
                        return internalServerError(throwable.getCause().getMessage());
                    }
                    return internalServerError("Failed to load contact details");
                });
    }
    public CompletionStage<Result> deleteContact(int id) {
        return contactService.removeContact(id)
                .thenApplyAsync(success -> {
                    if (success) {
                        return redirect(routes.ContactsController.index())
                                .flashing("success", "Contact deleted successfully");
                    } else {
                        return notFound("Contact not found");
                    }
                })
                .exceptionally(throwable -> {
                    if (throwable.getCause() instanceof SQLException) {
                        return internalServerError("Failed to delete contact: Database error");
                    }
                    return internalServerError("System error occurred");
                });
    }

}