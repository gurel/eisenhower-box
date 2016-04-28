package dao;

import models.User;
import scala.concurrent.duration.Duration;

import java.util.concurrent.CompletionStage;

/**
 * Created by gurelerceis on 26/04/16.
 */
public interface UserDAO {
	/**
	 * {@code timeout} defaults to {@link Duration} of 1 second.
	 *
	 * @see UserDAO#validateUser(String, String, Duration)
	 */
	CompletionStage<User> validateUser(String username, String password);

	/**
	 * This function will fetch the user checking the password on the {@link User} object.
	 * If the password doesn't match it will return null
	 *
	 * @param username username of the {@link User}
	 * @param password hashed password to compare with
	 * @param timeout Timeout duration of this request
	 * @return {@link CompletionStage<User>} completable future that will return the {@link User}
	 */
	CompletionStage<User> validateUser(String username, String password, Duration timeout);

	/**
	 * {@code timeout} defaults to {@link Duration} of 1 second.
	 *
	 * @see UserDAO#saveUser(User, Duration)
	 */
	CompletionStage<User> saveUser(User user);

	/**
	 * This function will save/update the given User depending on it's id
	 * @param user The {@link User} object to be persisted
	 * @param timeout Timeout duration of this request
	 * @return {@link CompletionStage<User>} completable future that will return the updated {@link User}
	 */
	CompletionStage<User> saveUser(User user, Duration timeout);

	/**
	 * {@code timeout} defaults to {@link Duration} of 1 second.
	 *
	 * @see UserDAO#getUser(String, Duration)
	 */
	CompletionStage<User> getUser(String username);

	/**
	 * This function will return the User with the given username
	 * @param username username of the User
	 * @param timeout Timeout duration of this request
	 * @return {@link CompletionStage<User>} completable future that will return the {@link User}
	 */
	CompletionStage<User> getUser(String username, Duration timeout);
}
