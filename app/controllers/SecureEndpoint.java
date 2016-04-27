package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

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
