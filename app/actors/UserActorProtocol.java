package actors;

import models.Task;
import models.User;

/**
 * Created by gurelerceis on 18/04/16.
 */
public class UserActorProtocol {
	public static class GetUser {
		public final String userID;

		public GetUser(String userID) {
			this.userID = userID;
		}
	}
	public static class GetUserByUserName {
		public final String username;

		public GetUserByUserName(String username) {
			this.username = username;
		}
	}
	public static class SaveUser {
		public final User user;

		public SaveUser(User user) {
			this.user = user;
		}
	}
}
