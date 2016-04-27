import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.*;

import play.libs.Json;
import play.test.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

public class IntegrationTest extends WithApplication {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */
    @Test
    public void testGetTaskList() {
        running(testServer(3333, provideApplication()), HTMLUNIT, browser -> {
            browser.goTo("http://localhost:3333/task");
			JsonNode response = Json.parse(browser.pageSource());

			// Check if value is returned
			assertNotNull(response);

			// Check if the returned data is an array
			assertTrue(response instanceof ArrayNode);

			ArrayNode arrayNode = (ArrayNode) response;
			response = arrayNode.get(0);

			// check the first item in the array is an array
			assertTrue(response instanceof ObjectNode);
			ObjectNode objectNode = (ObjectNode) response;

			// check fields in that object
			response = objectNode.get("id");
			assertTrue(response instanceof TextNode);
			response = objectNode.get("message");
			assertTrue(response instanceof TextNode);
			response = objectNode.get("urgency");
			assertTrue(response instanceof IntNode);
			response = objectNode.get("importance");
			assertTrue(response instanceof IntNode);
        });
    }

	@Test
	public void testGetTaskByID() {
		running(testServer(3333, provideApplication()), HTMLUNIT, browser -> {
			browser.goTo("http://localhost:3333/task");
			JsonNode response = Json.parse(browser.pageSource());
			ArrayNode arrayNode = (ArrayNode) response;
			response = arrayNode.get(0);
			ObjectNode objectNode = (ObjectNode) response;

			// check fields in that object
			String id = objectNode.get("id").textValue();
			browser.goTo("http://localhost:3333/task/" + id);
			response = Json.parse(browser.pageSource());

			assertTrue(response instanceof ObjectNode);
			objectNode = (ObjectNode) response;

			response = objectNode.get("id");
			assertTrue(response instanceof TextNode);
			response = objectNode.get("message");
			assertTrue(response instanceof TextNode);
			response = objectNode.get("urgency");
			assertTrue(response instanceof IntNode);
			response = objectNode.get("importance");
			assertTrue(response instanceof IntNode);
		});
	}

	@Test
	public void testSaveTask() {
		running(testServer(3333, provideApplication()), HTMLUNIT, browser -> {
//			ObjectNode data = new ObjectNode();
//			RequestBuilder request = new RequestBuilder().method("POST")
//					.bodyJson(jsonNode)
//					.uri(controllers.routes.MyController.myAction().url());

		});
	}
}
