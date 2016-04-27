package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.inject.Inject;
import dynamodb.DynamoDBClient;
import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskActor extends UntypedActor {

	public static Props props = Props.create(TaskActor.class);

	@Inject
	DynamoDBClient dynamoclient;

	public TaskActor() {
		/*
		http://doc.akka.io/docs/akka/snapshot/java/lambda-actors.html
		Warning
		The Java with lambda support part of Akka is marked as
		“experimental” as of its introduction in Akka 2.3.0. We will
		continue to improve this API based on our users’ feedback,
		which implies that while we try to keep incompatible changes
		to a minimum, but the binary compatibility guarantee for
		maintenance releases does not apply to the
		akka.actor.AbstractActor, related classes and the
		akka.japi.pf package.
		 */
		/*receive(ReceiveBuilder.
			match(TaskActorProtocol.GetTask.class, mes -> {
				sender().tell(new Task("sAD", 0, 12), self());
			}).
			match(TaskActorProtocol.SaveTask.class, mes -> {
				sender().tell(mes.task, self());
			}).
			match(TaskActorProtocol.GetTaskList.class, mes -> {
				ArrayList<Task> list = new ArrayList<Task>();
				list.add(new Task("sAD", 0, 123));
				list.add(new Task("sAD", 0, 123));
				list.add(new Task("sAD", 0, 123));
				sender().tell(list, self());
			}).
			build());*/
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof TaskActorProtocol.GetTask) {
			Task task = dynamoclient.get(Task.class, ((TaskActorProtocol.GetTask) message).taskID);
			sender().tell(task, self());
		}else if (message instanceof TaskActorProtocol.SaveTask) {
			Task updatedTask = dynamoclient.save(((TaskActorProtocol.SaveTask) message).task);
			sender().tell(updatedTask, self());
		}else if (message instanceof TaskActorProtocol.GetTaskList) {
			Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
			eav.put(":val1", new AttributeValue().withS(((TaskActorProtocol.GetTaskList) message).userID));

			DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
					.withFilterExpression("userID = :val1")
					.withExpressionAttributeValues(eav);

			final List<Task> tasks = dynamoclient.get(Task.class, scanExpression);

			sender().tell(tasks, self());
		}
	}
}
