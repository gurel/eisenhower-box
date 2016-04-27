package dao;

import models.Task;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface TaskDAO {

	CompletionStage<Task> getByID(String taskID);

	CompletionStage<Task> getByID(String taskID, long timeout);

	CompletionStage<Task> getByID(String taskID, Duration timeout);

	CompletionStage<List<Task>> getList(String userID);

	CompletionStage<List<Task>> getList(String userID, long timeout);

	CompletionStage<List<Task>> getList(String userID, Duration timeout);

	CompletionStage<Task> save(Task task);

	CompletionStage<Task> save(Task task, long timeout);

	CompletionStage<Task> save(Task task, Duration timeout);
}
