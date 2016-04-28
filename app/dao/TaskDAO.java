package dao;

import models.Task;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface TaskDAO {

	/**
	 * {@code timeout} defaults to {@link Duration} of 1 second.
	 *
	 * @see TaskDAO#getByID(String, Duration)
	 */
	CompletionStage<Task> getByID(String taskID);

	/**
	 * {@code timeout} converted to {@link Duration}.
	 *
	 * @see TaskDAO#getByID(String, Duration)
	 */
	CompletionStage<Task> getByID(String taskID, long timeout);

	/**
	 * This function will lookup a task
	 *
	 * @param taskID Fetched task ID
	 * @param timeout Timeout duration of this request
	 * @return {@link CompletionStage<Task>} completable future that will return the task
	 */
	CompletionStage<Task> getByID(String taskID, Duration timeout);

	/**
	 * {@code timeout} defaults to {@link Duration} of 1 second.
	 *
	 * @see TaskDAO#getList(String, Duration)
	 */
	CompletionStage<List<Task>> getList(String userID);

	/**
	 * {@code timeout} converted to {@link Duration}.
	 *
	 * @see TaskDAO#getByID(String, Duration)
	 */
	CompletionStage<List<Task>> getList(String userID, long timeout);

	/**
	 * This function will return the list of Tasks created by the user.
	 * @param userID
	 * @param timeout Timeout duration of this request
	 * @return {@link CompletionStage<List<Task>>} completable future that will return the task
	 */
	CompletionStage<List<Task>> getList(String userID, Duration timeout);

	/**
	 * {@code timeout} defaults to {@link Duration} of 1 second.
	 *
	 * @see TaskDAO#save(Task)
	 */
	CompletionStage<Task> save(Task task);

	/**
	 * {@code timeout} converted to {@link Duration}.
	 *
	 * @see TaskDAO#save(Task)
	 */
	CompletionStage<Task> save(Task task, long timeout);

	/**
	 * This function will save/update the given {@link Task} depending on it's id
	 * @param task Task to be saved or updated
	 * @param timeout Timeout duration of this request
	 * @return {@link CompletionStage<Task>} completable future that will return the updated task
	 */
	CompletionStage<Task> save(Task task, Duration timeout);
}
