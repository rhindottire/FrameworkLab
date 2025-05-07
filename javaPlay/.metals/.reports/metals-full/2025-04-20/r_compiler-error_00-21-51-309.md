file:///D:/GitHub/lecture-projects/FrameworkLab/javaPlay/app/repositories/BooksRepository.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file:///D:/GitHub/lecture-projects/FrameworkLab/javaPlay/app/repositories/BooksRepository.java
text:
```scala
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
	dotty.tools.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:31)
	dotty.tools.pc.SimpleCollector.<init>(PcCollector.scala:351)
	dotty.tools.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:63)
	dotty.tools.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:88)
	dotty.tools.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:111)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator