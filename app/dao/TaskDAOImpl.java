package dao;

import actors.TaskActorProtocol;
import akka.actor.ActorRef;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import models.Task;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.japi.Util.classTag;
import static akka.pattern.Patterns.ask;

@Singleton
public class TaskDAOImpl implements TaskDAO {
	@Inject
	@Named("task-actor")
	ActorRef taskActor;

	@Override
	public CompletionStage<Task> getByID(String taskID) {
		return getByID(taskID, 1000);
	}

	@Override
	public CompletionStage<Task> getByID(String taskID, long timeout) {
		return getByID(taskID, Duration.create(timeout, TimeUnit.MILLISECONDS));
	}

	@Override
	public CompletionStage<Task> getByID(String taskID, Duration timeout) {
		Future<Task> future = ask(
				taskActor,
				new TaskActorProtocol.GetTask(taskID),
				timeout.toMillis()
		).mapTo(classTag(Task.class));

		return FutureConverters.toJava(future);
	}

	@Override
	public CompletionStage<List<Task>> getList(String userID) {
		return getList(userID, 1000);
	}

	@Override
	public CompletionStage<List<Task>> getList(String userID, long timeout) {
		return getList(userID, Duration.create(timeout, TimeUnit.MILLISECONDS));
	}

	@Override
	public CompletionStage<List<Task>> getList(String userID, Duration timeout) {
		Future<List> arrayListFuture = ask(
				taskActor,
				new TaskActorProtocol.GetTaskList(userID),
				timeout.toMillis()
		).mapTo(classTag(List.class));

		return FutureConverters.toJava(arrayListFuture).thenApply((tasklist) -> (List<Task>) tasklist);
	}

	@Override
	public CompletionStage<Task> save(Task task) {
		return save(task, 1000);
	}

	@Override
	public CompletionStage<Task> save(Task task, long timeout) {
		return save(task, Duration.create(timeout, TimeUnit.MILLISECONDS));
	}

	@Override
	public CompletionStage<Task> save(Task task, Duration timeout) {
		Future<Task> future = ask(
				taskActor,
				new TaskActorProtocol.SaveTask(task),
				timeout.toMillis()
		).mapTo(classTag(Task.class));

		return FutureConverters.toJava(future);
	}
}
