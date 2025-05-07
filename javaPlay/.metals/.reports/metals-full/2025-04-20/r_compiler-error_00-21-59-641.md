file:///D:/GitHub/lecture-projects/FrameworkLab/javaPlay/app/repositories/ContactsRepository.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 1405
uri: file:///D:/GitHub/lecture-projects/FrameworkLab/javaPlay/app/repositories/ContactsRepository.java
text:
```scala
package repositories;

import models.Contact;
import play.db.Database;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ContactsRepository {

    private final Database db;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public ContactsRepository(Database db, DatabaseExecutionContext context) {
        this.db = db;
        this.executionContext = context;
    }

    public CompletionStage<List<Contact>> getContacts() {
        return CompletableFuture.supplyAsync(() -> {
            return db.withConnection(connection -> {
                List<Contact> contacts = new ArrayList<>();
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM Contact")) {
                    while (rs.next()) {
                        contacts.add(extractContactFromResultSet(rs));
                    }
                }
                return contacts;
            });
        }, executionContext);
    }

    // Create
    public CompletionStage<Contact> cr@@eateContact(Contact contact) {
        return CompletableFuture.supplyAsync(() -> {
            return db.withConnection(connection -> {
                String sql = "INSERT INTO Contact (name, email, phone) VALUES (?, ?, ?)";
                try (PreparedStatement pState = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    pState.setString(1, contact.getName());
                    pState.setString(2, contact.getEmail());
                    pState.setString(3, contact.getPhone());

                    int affectedRows = pState.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Creating contact failed, no rows affected.");
                    }

                    try (ResultSet generatedKeys = pState.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);
                            return new Contact(id, contact.getName(), contact.getEmail(), contact.getPhone());
                        } else {
                            throw new SQLException("Creating contact failed, no ID obtained.");
                        }
                    }
                }
            });
        }, executionContext);
    }

    // Read
    public CompletionStage<Contact> getContactById(int id) {
        return CompletableFuture.supplyAsync(() -> {
            return db.withConnection(connection -> {
                String sql = "SELECT * FROM Contact WHERE id = ?";
                try (PreparedStatement pState = connection.prepareStatement(sql)) {
                    pState.setInt(1, id);
                    try (ResultSet rs = pState.executeQuery()) {
                        if (rs.next()) {
                            return extractContactFromResultSet(rs);
                        }
                    }
                }
                return null;
            });
        }, executionContext);
    }

    // Update
    public CompletionStage<Contact> updateContact(Contact contact) {
        return CompletableFuture.supplyAsync(() -> {
            return db.withConnection(connection -> {
                String sql = "UPDATE Contact SET name = ?, email = ?, phone = ? WHERE id = ?";
                try (PreparedStatement pState = connection.prepareStatement(sql)) {
                    pState.setString(1, contact.getName());
                    pState.setString(2, contact.getEmail());
                    pState.setString(3, contact.getPhone());
                    pState.setInt(4, contact.getId());

                    int affectedRows = pState.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Updating contact failed, no rows affected.");
                    }
                    return contact;
                }
            });
        }, executionContext);
    }

    // Delete
    public CompletionStage<Boolean> deleteContact(int id) {
        return CompletableFuture.supplyAsync(() -> {
            return db.withConnection(connection -> {
                String sql = "DELETE FROM Contact WHERE id = ?";
                try (PreparedStatement pState = connection.prepareStatement(sql)) {
                    pState.setInt(1, id);
                    int affectedRows = pState.executeUpdate();
                    return affectedRows > 0;
                }
            });
        }, executionContext);
    }

    // Helper method to extract Contact from ResultSet
    private Contact extractContactFromResultSet(ResultSet rs) throws SQLException {
        return new Contact(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }

}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:389)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator