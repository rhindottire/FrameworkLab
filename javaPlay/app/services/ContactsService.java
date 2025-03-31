package services;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletionStage;
import models.Contact;
import repositories.ContactsRepository;

public class ContactsService {
    private final ContactsRepository contactsRepository;

    @Inject
    public ContactsService(ContactsRepository contactsRepository) {
        this.contactsRepository = contactsRepository;
    }

    public CompletionStage<List<Contact>> getAllContacts() {
        return contactsRepository.getContacts()
                .exceptionally(ex -> {
                    throw new RuntimeException("Failed to load contact: " + ex.getCause().getMessage());
                });
    }
    public Contact getContactById(int id) {
        return contactsRepository.getContactById(id)
                .toCompletableFuture()
                .join(); //
    }

    public CompletionStage<Contact> addContact(Contact contact) {
        return contactsRepository.createContact(contact)
                .exceptionally(throwable -> {
                    if (throwable.getCause() instanceof SQLException) {
                        throw new RuntimeException("Database error: " + throwable.getCause().getMessage());
                    }
                    throw new RuntimeException("Unexpected error: " + throwable.getMessage());
                });
    }

    public CompletionStage<Contact> updateContact(Contact contact) {
        return contactsRepository.updateContact(contact)
                .thenApply(updatedContact -> {
                    if (updatedContact == null) {
                        throw new RuntimeException("Failed to update contact");
                    }
                    return updatedContact;
                });
    }
    public CompletionStage<Boolean> removeContact(int id) {
        return contactsRepository.deleteContact(id);
    }


}
