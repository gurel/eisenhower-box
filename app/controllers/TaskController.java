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

/**
 * This class is responsible for the Task CRUD operations.
 */
public class TaskController extends Controller {
	@Inject
	TaskDAO taskDAO;

	/**
	 * This function will return all of the tasks created by the user.
	 * It will use the userID stored in the session to do this.
	 *
	 * @return CompletionStage<Result> JSON encoded list of tasks.
	 */
	@Security.Authenticated(SecureEndpoint.class)
	public CompletionStage<Result> getList() {
		// Fetch the task list
		CompletionStage<List<Task>> task = taskDAO.getList(session("userID"), 100);

		// Encode and return a JSON node
		return task.
			thenApply(o -> ok(Json.toJson(o))).
			exceptionally(e -> internalServerError(e.getMessage()));
	}

	/**
	 * This function will return a single task
	 *
	 * @param taskID Id of the requested task
	 * @return CompletionStage<Result> JSON encoded task
	 */
	@Security.Authenticated(SecureEndpoint.class)
	public CompletionStage<Result> getByID(String taskID) {
		String userID = session("userID");

		// Fetch the task
		CompletionStage<Task> task = taskDAO.getByID(taskID, Duration.create("1s"));

		// Encode the result to a Json object
		return task.
			thenApply((Task o) -> {
				if (!o.getUserID().equals(userID)) {
					return badRequest("Not allowed to fetch this task due to access rights");
				}
				return ok(Json.toJson(o));
			}).
			exceptionally(e -> internalServerError(e.getMessage()));
	}

	/**
	 * This function will save/update a given task.
	 * The behavior will change depending on whether the id of a task is provided on the request.
	 *
	 * @return CompletionStage<Result> JSON encoded task after the save operation
	 */
	@Security.Authenticated(SecureEndpoint.class)
	@BodyParser.Of(BodyParser.Json.class)
	public CompletionStage<Result> saveTask() {
		// TODO: This function should first try to fetch the task to check whether it exists and
		// the current user has permission over it. This is intentionally omitted in the MVP.

		// Read body of request
		JsonNode json = request().body().asJson();

		// Don't permit empty messages
		if ( json.findPath("message").textValue() == null || json.findPath("message").textValue().isEmpty() ) {
			return CompletableFuture.completedFuture(badRequest("Task message is required"));
		}

		// Convert JsonNode to Task instance
		Task task = Json.fromJson(json, Task.class);

		// Set userID of task to the current user
		task.setUserID(session("userID"));

		// Save the Task
		CompletionStage<Task> savedTask = taskDAO.save(task);

		// Encode the saved task to a json object
		return savedTask.
			thenApply((_task) -> ok(Json.toJson(_task))).
			exceptionally(e -> internalServerError(e.getMessage()));
	}
}
