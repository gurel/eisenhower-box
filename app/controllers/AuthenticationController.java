package controllers;

import com.google.inject.Inject;
import com.typesafe.config.ConfigFactory;
import dao.UserDAO;
import models.User;
import org.apache.commons.codec.binary.Base64;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.login;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static com.amazonaws.services.s3.internal.Constants.HMAC_SHA1_ALGORITHM;
import static play.data.Form.form;

public class AuthenticationController extends Controller {
	@Inject
	private UserDAO userDAO;

	@Inject
	FormFactory formFactory;

	public static class Login {

		@Constraints.Required
		public String email;
		@Constraints.Required
		public String password;

		public String validate() {
			if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
				return "Invalid user or password";
			}
			return null;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	public static class Register {

		@Constraints.Required
		public String email;
		@Constraints.Required
		public String password;
		@Constraints.Required
		public String password2;

		public String validate() {
			if (email == null || email.isEmpty() ||
					password == null || password.isEmpty() ||
					!password.equals(password2)) {
				return "Invalid user or password";
			}
			return null;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPassword2() {
			return password2;
		}

		public void setPassword2(String password2) {
			this.password2 = password2;
		}
	}

	public Result login() {
		return ok(
				login.render(formFactory.form(Login.class), null)
		);
	}
	public Result register() {
		return ok(
				login.render(null, formFactory.form(Register.class))
		);
	}
	public Result logout() {
		session().clear();
		return redirect(
				routes.HomeController.index()
		);
	}
	public CompletionStage<Result> authenticate() {
		Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();

		if (loginForm.hasErrors()) {
			return CompletableFuture.completedFuture(badRequest(login.render(loginForm, null)));
		}

		CompletionStage<String> booleanCompletionStage = userDAO.validateUser(loginForm.get().email, hashPassword(loginForm.get().password));
		System.out.println(hashPassword(loginForm.get().password));
		final Http.Session session = session();

		return booleanCompletionStage.thenApply((userID) -> {
			if (userID == null) {
				List<ValidationError> errors = new ArrayList<>();
				errors.add(0, new ValidationError("", "Invalid user or password"));
				loginForm.errors().put("", errors);

				return badRequest(login.render(loginForm, null));
			}else{
				session.clear();
				session.put("email", loginForm.get().email);
				session.put("userID", userID);
				return redirect(
						routes.HomeController.index()
				);
			}
		});
	}
	public CompletionStage<Result> signup() {
		Form<Register> registerForm = formFactory.form(Register.class).bindFromRequest();
		final Http.Session session = session();

		CompletableFuture<Result> future = new CompletableFuture<>();

		if (registerForm.hasErrors()) {
			future.complete(badRequest(login.render(null, registerForm)));
			return future;
		}

		final java.util.function.Function<Throwable, ? extends Void> errorhandler = (Throwable throwable) -> {
			List<ValidationError> errors = new ArrayList<>();
			errors.add(0, new ValidationError("", throwable.getMessage()));
			registerForm.errors().put("", errors);

			future.complete(badRequest(login.render(null, registerForm)));
			return null;
		};

		userDAO.getUser(registerForm.get().email).
				thenAccept((user) -> {
					if (user != null) {
						// If a user is found complete the future with a badRequest
						List<ValidationError> errors = new ArrayList<>();
						errors.add(0, new ValidationError("", "Email already exists!"));
						registerForm.errors().put("", errors);

						future.complete(badRequest(login.render(null, registerForm)));
					}else{
						// If a user is not found, create the user, create the session and redirect
						userDAO.saveUser(new User(registerForm.get().email, hashPassword(registerForm.get().password))).
								thenAccept((savedUser) -> {
									session.clear();
									session.put("email", registerForm.get().email);
									session.put("userID", savedUser.getUserID());
									future.complete(redirect(routes.HomeController.index()));
								}).
								exceptionally(errorhandler);
					}
				}).
				exceptionally(errorhandler);

		return future;
	}
	private String hashPassword(String data) {
		String secret = ConfigFactory.defaultApplication().getString("play.crypto.secret");
		try {
			SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(),HMAC_SHA1_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes());
			String result = new String(Base64.encodeBase64(rawHmac));
			return result;
		} catch (GeneralSecurityException e) {
			System.out.println("Unexpected error while creating hash: " + e.getMessage());
			throw new IllegalArgumentException();
		}
	}

}
/*
session.clear();
session.put("email", loginForm.get().email);
session.put("userID", savedUser.getUserID());
return redirect(
	routes.HomeController.index()
);
* */
