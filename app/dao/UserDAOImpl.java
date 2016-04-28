package dao;

import actors.TaskActorProtocol;
import actors.UserActorProtocol;
import akka.actor.ActorRef;
import akka.dispatch.OnComplete;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import models.Task;
import models.User;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import static akka.japi.Util.classTag;
import static akka.pattern.Patterns.ask;

/**
 * Created by gurelerceis on 26/04/16.
 */
public class UserDAOImpl implements UserDAO {
	@Inject
	@Named("user-actor")
	ActorRef userActor;

	@Override
	public CompletionStage<User> validateUser(String username, String password) {
		return validateUser(username, password, Duration.create('1', TimeUnit.SECONDS));
	}

	@Override
	public CompletionStage<User> validateUser(String username, String password, Duration timeout) {
		// Fetch the user with the given username
		Future<User> userFuture = ask(
				userActor,
				new UserActorProtocol.GetUserByUserName(username),
				timeout.toMillis()
		).mapTo(classTag(User.class));

		CompletionStage<User> userCompletionStage = FutureConverters.toJava(userFuture).
				thenApply((user) -> {
					// check whether the given password matches the user
					if (user.getPassword().equals(password)) {
						return user;
					} else {
						return null;
					}
				});

		return userCompletionStage;
	}

	@Override
	public CompletionStage<User> saveUser(User user) {
		return saveUser(user, Duration.create('1', TimeUnit.SECONDS));
	}

	@Override
	public CompletionStage<User> saveUser(User user, Duration timeout) {
		Future<User> userFuture = ask(
				userActor,
				new UserActorProtocol.SaveUser(user),
				timeout.toMillis()
		).mapTo(classTag(User.class));

		return FutureConverters.toJava(userFuture);
	}

	@Override
	public CompletionStage<User> getUser(String username) {
		return getUser(username, Duration.create('1', TimeUnit.SECONDS));
	}

	@Override
	public CompletionStage<User> getUser(String username, Duration timeout) {
		// Call actor to get the user with the given username
		Future<User> userFuture = ask(
				userActor,
				new UserActorProtocol.GetUserByUserName(username),
				timeout.toMillis()
		).mapTo(classTag(User.class));

		// Since Akka cannot return a null value, instead we send a empty User object
		// So if there is no id on the user translate that to null
		return FutureConverters.toJava(userFuture).
				thenApply((user) -> {
					// This checking is done because akka doesn't support null messages
					// Instead passed an dummy user back.
					if(user.getUserID() == null) {
						return null;
					}
					return user;
				});
	}
}
