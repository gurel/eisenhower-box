package dao;

import models.User;
import scala.concurrent.duration.Duration;

import java.util.concurrent.CompletionStage;

/**
 * Created by gurelerceis on 26/04/16.
 */
public interface UserDAO {
	CompletionStage<String> validateUser(String username, String password);
	CompletionStage<String> validateUser(String username, String password, Duration timeout);
	CompletionStage<User> saveUser(User user);
	CompletionStage<User> saveUser(User user, Duration timeout);
	CompletionStage<User> getUser(String username);
	CompletionStage<User> getUser(String username, Duration timeout);
}
