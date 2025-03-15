package controllers;

import models.Book;
import services.BooksService;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Controller;

import play.data.Form;
import play.data.FormFactory;

import play.i18n.MessagesApi;
import play.i18n.Messages;

import views.html.Books.*;

import javax.inject.Inject;
import java.util.Set;

public class BooksController extends Controller {

    private final BooksService booksService;
    private final FormFactory formFactory;
    private final MessagesApi messagesApi;

    @Inject
    public BooksController(BooksService booksService, FormFactory formFactory, MessagesApi messagesApi) {
        this.booksService = booksService;
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
    }

    public Result index() {
        try {
            return ok(index.render(booksService.getAllBooks()));
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    public Result createBook(Http.Request request) {
        try {
            return ok(create.render(formFactory.form(Book.class), request, messagesApi.preferred(request).asScala()));
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    public Result saveBook(Http.Request request) {
        try {
            Form<Book> bookForm = formFactory.form(Book.class).bindFromRequest(request);
            booksService.addBook(bookForm.get());
            return redirect(routes.BooksController.index());
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    public Result editBook(Integer id, Http.Request request) {
        try {
            return ok(edit.render(
                    formFactory.form(Book.class).fill(booksService.getBookById(id)),
                    request,
                    messagesApi.preferred(request).asScala()
            ));
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }
    public Result updateBook(Integer id, Http.Request request) {
        try {
            Form<Book> bookForm = formFactory.form(Book.class).bindFromRequest(request);
            booksService.updateBook(id, bookForm.get());
            return redirect(routes.BooksController.index());
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    public Result showBook(Integer id) {
        try {
            return ok(show.render(booksService.getBookById(id)));
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }
    public Result removeBook(Integer id) {
        try {
            booksService.removeBook(id);
            return redirect(routes.BooksController.index());
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }
}