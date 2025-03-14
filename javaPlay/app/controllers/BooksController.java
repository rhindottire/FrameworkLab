package controllers;

import models.Book;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Controller;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.i18n.Messages;

//import views.html.Books.index;
//import views.html.Books.create;
import views.html.Books.*;

import javax.inject.Inject;
import java.util.Set;

//                           each controller should extends this controller
public class BooksController extends Controller {

    private final FormFactory formFactory;
    private final MessagesApi messagesApi;

    @Inject
    public BooksController(FormFactory formFactory, MessagesApi messagesApi) {
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
    }

    public Result index() {
        Set<Book> books = Book.allBooks();
        return ok(index.render(books)); // /books
    }

    public Result createBook(Http.Request request) { // /books/create
        Form<Book> bookForm = formFactory.form(Book.class);
        return ok(create.render(bookForm, request, messagesApi.preferred(request).asScala()));
    }
    public Result saveBook(Http.Request request) { // /books/create
        Form<Book> bookForm = formFactory.form(Book.class).bindFromRequest(request);
        if (bookForm.hasErrors()) {
            return badRequest(create.render(bookForm, request, messagesApi.preferred(request).asScala()));
        }
        Book book = bookForm.get();
        System.out.println("Book added: " + book.getTitle() + " - " + book.getAuthor());
        Book.addBook(book);
//        System.out.println("Book added: " + book.title + " - " + book.author);
//        System.out.println("Total books: " + Book.allBooks().size());
        return redirect(routes.BooksController.index());
    }

    public Result editBook(Integer id, Http.Request request) { // /books/edit/:id
        Book book = Book.findById(id);
        if (book == null) {
            return notFound("404 NOT FOUND");
        }
        Form<Book> bookForm = formFactory.form(Book.class).fill(book);
        return ok(edit.render(bookForm, request, messagesApi.preferred(request).asScala()));
    }
    public Result updateBook(Integer id, Http.Request request) { // /books/edit/:id
        Form<Book> bookForm = formFactory.form(Book.class).bindFromRequest(request);
        if (bookForm.hasErrors()) {
            return badRequest("Invalid form submission");
        }
        Book book = bookForm.get();
        Book oldBook = Book.findById(id);
        if (oldBook == null) {
            return notFound("404 NOT FOUND");
        }
        oldBook.title = book.title;
        oldBook.price = book.price;
        oldBook.author = book.author;
        return redirect(routes.BooksController.index());
    }

    public Result showBook(Integer id) { // /books/:id
        Book book =  Book.findById(id);
        if (book == null) {
            return notFound("404 NOT FOUND");
        }
        return ok(show.render(book));
    }
    public Result removeBook(Integer id) { // /books/delete/:id
        Book book = Book.findById(id);
        if (book == null) {
            return notFound("404 NOT FOUND");
        }
        Book.removeBook(id);
        return redirect(routes.BooksController.index());
    }
}
