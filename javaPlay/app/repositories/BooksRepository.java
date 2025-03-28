package repositories;

import models.Book;
import repositories.DatabaseExecutionContext;
import play.db.Database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.*;
import java.sql.SQLException;

@Singleton
public class BooksRepository {

    private final Database db;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public BooksRepository(Database db, DatabaseExecutionContext context) {
        this.db = db;
        this.executionContext = context;
    }

    public CompletionStage<List<Book>> getBooks() {
        return CompletableFuture.supplyAsync(() -> {
            return db.withConnection(connection -> {
                List<Book> books = new ArrayList<>();
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM Book")) {
                    while (rs.next()) {
                        books.add(new Book(
                                (int) rs.getInt("id"),
                                rs.getString("title"),
                                (int) rs.getInt("price"),
                                rs.getString("Author")
                        ));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return books;
            });
        }, executionContext);
    }
}