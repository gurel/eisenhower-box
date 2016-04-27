package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.inject.Inject;
import dynamodb.DynamoDBClient;
import models.Task;
import models.User;

import java.util.ArrayList;
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
			User task = dynamoclient.get(
					User.class,
					((UserActorProtocol.GetUser) message).userID
			);

			sender().tell(task, self());
		}else if(message instanceof UserActorProtocol.GetUserByUserName) {
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
				sender().tell(new User(), self());
			}else{
				sender().tell(users.get(0), self());
			}

		}else if (message instanceof UserActorProtocol.SaveUser) {
			User updatedTask = dynamoclient.save(
					((UserActorProtocol.SaveUser) message).user
			);

			sender().tell(updatedTask, self());
		}
	}
}
