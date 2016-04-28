package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.inject.Inject;
import dynamodb.DynamoDBClient;
import models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActor extends UntypedActor {

	public static Props props = Props.create(UserActor.class);

	@Inject
	DynamoDBClient dynamoclient;

	public UserActor() {

	}

	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof UserActorProtocol.GetUser) {
			// Fetch the requested User from DynamoDB
			User task = dynamoclient.get(
					User.class,
					((UserActorProtocol.GetUser) message).userID
			);

			sender().tell(task, self());
		}else if(message instanceof UserActorProtocol.GetUserByUserName) {
			// Create a DynamoDBScanExpression expression to scan the DynamoDB Document and find
			// {@link Users} with the given Username
			Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
			eav.put(":val1", new AttributeValue().withS(((UserActorProtocol.GetUserByUserName) message).username));

			DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
					.withFilterExpression("username = :val1")
					.withExpressionAttributeValues(eav);

			final List<User> users = dynamoclient.get(
					User.class,
					scanExpression
			);
			if (users.size() < 1) {
				// No user found with the given username
				// Return a dummy user because akka doesn't support null values
				sender().tell(new User(), self());
			}else{
				sender().tell(users.get(0), self());
			}

		}else if (message instanceof UserActorProtocol.SaveUser) {
			// Persist the requested Task from DynamoDB
			User updatedTask = dynamoclient.save(
					((UserActorProtocol.SaveUser) message).user
			);

			sender().tell(updatedTask, self());
		}
	}
}
