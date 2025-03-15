package services;

import models.Book;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class BooksService {

    public Set<Book> getAllBooks() {
        return Book.allBooks();
    }

    public Book getBookById(Integer id) throws Exception {
        Book book = Book.findById(id);
        if (book == null) {
            throw new Exception("Buku tidak ditemukan");
        }
        return book;
    }

    public void addBook(Book book) {
        Book.addBook(book);
    }

    public void updateBook(Integer id, Book updatedBook) throws Exception {
        Book existingBook = Book.findById(id);
        if (existingBook == null) {
            throw new Exception("Buku tidak ditemukan");
        }
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setAuthor(updatedBook.getAuthor());
    }

    public void removeBook(Integer id) throws Exception {
        Book book = Book.findById(id);
        if (book == null) {
            throw new Exception("Buku tidak ditemukan");
        }
        Book.removeBook(id);
    }
}