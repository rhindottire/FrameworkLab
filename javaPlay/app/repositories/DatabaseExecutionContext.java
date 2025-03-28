package repositories;

import org.apache.pekko.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "database.dispatcher" thread pool
 * https://www.playframework.com/documentation/2.6.3/JavaDatabase
 */
public class DatabaseExecutionContext extends CustomExecutionContext {
    @Inject
    public DatabaseExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "database.dispatcher");
    }
}