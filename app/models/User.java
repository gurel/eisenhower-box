package models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.io.Serializable;
import java.util.Date;

/**
 * User model class
 */
@DynamoDBTable(tableName="users")
public class User implements Serializable {


	private String userID;
	private String username;
	private String password;
	private Date lastLoginDate;
	private Date signupDate;

	public User() {
	}

	public User(String username, String password) {
		this(username, password, new Date(), new Date());
	}

	public User(String username, String password, Date lastLoginDate, Date signupDate) {
		this.username = username;
		this.password = password;
		this.lastLoginDate = lastLoginDate;
		this.signupDate = signupDate;
	}

	@DynamoDBHashKey(attributeName="id")
	@DynamoDBAutoGeneratedKey
	public String getUserID() { return userID; }
	public void setUserID(String userID) { this.userID = userID; }

	@DynamoDBAttribute(attributeName="username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@DynamoDBAttribute(attributeName="password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@DynamoDBAttribute(attributeName="lastLoginDate")
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	@DynamoDBAttribute(attributeName="signupDate")
	public Date getSignupDate() {
		return signupDate;
	}

	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}
}
