package models;

import java.util.HashSet;
import java.util.Set;

public class Book {
    public  Integer id;
    public  String title;
    public  Integer price;
    public  String author;

    public Book() {
        // Contructor
    }

    public Book (Integer id, String title, Integer price, String author) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.author = author;
    }

    // Getter dan Setter (diperlukan agar Play dapat memetakan form)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    private static final Set<Book> books;
    static {
        books = new HashSet<Book>();
        books.add(new Book(1, "Python", 100, "Edho"));
        books.add(new Book(2, "C++", 100, "Ridho"));
        books.add(new Book(3, "Java", 100, "Dodo"));
    }

    public static Set<Book> allBooks() {
        return books;
    }

    public static Book findById(Integer id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static void addBook(Book book) {
        books.add(book);
    }
    public static boolean removeBook(Integer id) {
        return books.remove(findById(id));
    }
}
