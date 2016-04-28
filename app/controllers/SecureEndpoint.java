package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * This class will be used to define a secure endpoint.
 *
 * Sample Usage:
 * @Security.Authenticated(SecureEndpoint.class)
 * public Result index() {}
 *
 * It will try to confirm that userID variable exists in the session.
 * If not it will redirect the user to the login page.
 */
public class SecureEndpoint extends Security.Authenticator {

	@Override
	public String getUsername(Http.Context ctx) {
		return ctx.session().get("userID");
	}

	@Override
	public Result onUnauthorized(Http.Context ctx) {
		return redirect(routes.AuthenticationController.login());
	}
}
