import com.google.inject.Guice;
import com.google.inject.Inject;
import dao.TaskDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.api.Environment;
import play.api.inject.guice.GuiceApplicationBuilder;
import play.api.inject.guice.GuiceApplicationLoader;
import play.twirl.api.Content;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Your new application is ready.");
        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("<html"));
    }


}
