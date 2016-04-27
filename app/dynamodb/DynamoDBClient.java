package dynamodb;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.ConfigFactory;
import models.Task;
import models.User;
import play.api.Configuration;
import play.api.Play;
import play.api.PlayConfig;
import play.api.libs.crypto.HMACSHA1CookieSigner;

import java.io.InvalidObjectException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Singleton
public class DynamoDBClient {
	private AmazonDynamoDBClient client;
	private DynamoDBMapper mapper;

	public DynamoDBClient() {
		this.client = new AmazonDynamoDBClient(new ClientConfiguration());
		this.client.setEndpoint(
			ConfigFactory.defaultApplication().getString("dynamodb.endpoint")
		);

		mapper = new DynamoDBMapper(this.client);

		User admin = new User("admin@admin.com", "mYUFdEj4yUZ0NmeiLsUXH5q87jI=");

		try {
			createTable(User.class, null);

			mapper.save(admin);

		} catch (Exception e ) {
			System.out.printf(e.getMessage());
		}
		try {
			createTable(Task.class, null);

			ArrayList<Task> list = new ArrayList<Task>();
			list.add(new Task(admin.getUserID(), "This task is urgent ", 0, 123, Task.Status.INPROGRESS));
			list.add(new Task(admin.getUserID(), "sAD", 0, 123, Task.Status.INPROGRESS));
			list.add(new Task(admin.getUserID(), "sAD", 0, 123, Task.Status.INPROGRESS));

			for (Task task : list) {
				mapper.save(task);
			}
		} catch (Exception e) {
			System.out.printf(e.getMessage());
		}
	}

	/*
	* Persistance Methods
	*/
	public <T extends Object> T save(T instance) throws InvalidObjectException {
		Objects.requireNonNull(instance);
		checkTableAnnotation(instance);

		mapper.save(instance);

		return instance;
	}

	public <T extends Object> List<T> getAll(Class<T> clazz) throws InvalidObjectException {
		checkTableAnnotation(clazz);

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		List<T> query = mapper.scan(clazz, scanExpression);

		return query;
	}



	public <T> T get(Class<T> clazz, String primaryKeyValue) throws InvalidObjectException {
		Objects.requireNonNull(clazz);
		checkTableAnnotation(clazz);

		T instance = mapper.load(clazz, primaryKeyValue);

		return instance;
	}

	public <T extends Object> List<T> get(Class<T> clazz, DynamoDBScanExpression scanExpression) throws InvalidObjectException {
		checkTableAnnotation(clazz);

		List<T> query = mapper.scan(clazz, scanExpression);

		return query;
	}

	public <T> List<T> get(Class<T> clazz, DynamoDBQueryExpression expression) throws InvalidObjectException {
		Objects.requireNonNull(clazz);
		checkTableAnnotation(clazz);

		final List<T> query = mapper.query(clazz, expression);

		return query;
	}

	public <T> boolean createTable(Class<T> clazz, ProvisionedThroughput throughput) throws Exception {
		CreateTableRequest req = mapper.generateCreateTableRequest(clazz);
		// Table provision throughput is still required since it cannot be specified in your POJO
		req.setProvisionedThroughput(throughput != null ? throughput : new ProvisionedThroughput(
				ConfigFactory.defaultApplication().getLong("dynamodb.readCapacityUnits"),
				ConfigFactory.defaultApplication().getLong("dynamodb.writeCapacityUnits")
		));
		// Fire off the CreateTableRequest using the low-level client
		this.client.createTable(req);

		return true;
	}
	/*
	* Util functions
	*/
	private <T> void checkTableAnnotation(T instance) throws InvalidObjectException {
		Objects.requireNonNull(instance);

		Class clazz;
		if(instance instanceof Class){
			clazz = (Class) instance;
		}else{
			clazz = instance.getClass();
		}
		Annotation declaredAnnotation = clazz.getDeclaredAnnotation(DynamoDBTable.class);
		if(declaredAnnotation == null){
			throw new InvalidObjectException("Only DynamoDBTable annotated objects can be saved");
		}
	}
}
