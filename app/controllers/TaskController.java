package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import dao.TaskDAO;
import models.Task;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class TaskController extends Controller {
	@Inject
	TaskDAO taskDAO;

	@Security.Authenticated(SecureEndpoint.class)
	public CompletionStage<Result> getList() {
		// This method shows how to use Await class to perform a blocking
		// call and still remain Non-Blocking on the play
		CompletionStage<List<Task>> task = taskDAO.getList(session("userID"), 100);

		return task.
			thenApply(o -> {
				JsonNode json = Json.toJson(o);
				return ok(json);
			}).
			exceptionally(e -> internalServerError(e.getMessage()));
	}

	@Security.Authenticated(SecureEndpoint.class)
	public CompletionStage<Result> getByID(String taskID) {
		// This method demonstrates how to use non-blocking with a callback
		// format
		CompletionStage<Task> task = taskDAO.getByID(taskID, Duration.create("1s"));

		return task.
			thenApply((Task o) -> {
				JsonNode json = Json.toJson(o);
				return ok(json);
			}).
			exceptionally(e -> internalServerError(e.getMessage()));
	}

	@Security.Authenticated(SecureEndpoint.class)
	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> saveTask() {
		JsonNode json = request().body().asJson();

		if ( json.findPath("message").textValue() == null ) {
			return CompletableFuture.completedFuture(badRequest("Task message is required"));
		}

		Task task = Json.fromJson(json, Task.class);
		task.setUserID(session("userID"));
		CompletionStage<Task> savedTask = taskDAO.save(task);

		return savedTask.
			thenApply((_task) -> {
				JsonNode _taskJson = Json.toJson(_task);
				return ok(_taskJson);
			}).
			exceptionally(e -> internalServerError(e.getMessage()));
	}
}
