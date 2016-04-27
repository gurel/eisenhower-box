package actors;

import models.Task;

/**
 * Created by gurelerceis on 18/04/16.
 */
public class TaskActorProtocol {
	public static class GetTask {
		public final String taskID;

		public GetTask(String taskID) {
			this.taskID = taskID;
		}
	}
	public static class SaveTask {
		public final Task task;

		public SaveTask(Task task) {
			this.task = task;
		}
	}

	public static class GetTaskList {
		public final String userID;

		public GetTaskList(String userID) {
			this.userID = userID;
		}
	}
}
