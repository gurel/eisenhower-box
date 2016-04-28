package controllers;

import play.mvc.*;

import views.html.*;

/**
 *
 */
public class HomeController extends Controller {

    /**
     *
	 *
	 * @return Result
	 */
	@Security.Authenticated(SecureEndpoint.class)
    public Result index() {
        return ok(index.render());
    }

}
