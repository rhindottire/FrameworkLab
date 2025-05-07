file:///D:/GitHub/lecture-projects/FrameworkLab/javaPlay/app/services/ContactsService.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 1781
uri: file:///D:/GitHub/lecture-projects/FrameworkLab/javaPlay/app/services/ContactsService.java
text:
```scala
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
                    return up@@datedContact;
                });
    }
    public CompletionStage<Boolean> removeContact(int id) {
        return contactsRepository.deleteContact(id);
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