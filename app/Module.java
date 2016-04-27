import actors.TaskActor;
import actors.UserActor;
import com.google.inject.AbstractModule;
import dao.TaskDAO;
import dao.TaskDAOImpl;
import dao.UserDAO;
import dao.UserDAOImpl;
import dynamodb.DynamoDBClient;
import play.libs.akka.AkkaGuiceSupport;
import services.ApplicationTimer;

import java.time.Clock;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule implements AkkaGuiceSupport {

    @Override
    public void configure() {
		bind(DynamoDBClient.class).asEagerSingleton();

		bindActor(TaskActor.class, "task-actor");
		bindActor(UserActor.class, "user-actor");

		bind(TaskDAO.class).to(TaskDAOImpl.class).asEagerSingleton();
		bind(UserDAO.class).to(UserDAOImpl.class).asEagerSingleton();

        // Use the system clock as the default implementation of Clock
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        // Ask Guice to create an instance of ApplicationTimer when the
        // application starts.
        bind(ApplicationTimer.class).asEagerSingleton();
    }

}
