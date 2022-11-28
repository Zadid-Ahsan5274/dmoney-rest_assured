package controller;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.UserModel;
import setup.Setup;
import utils.Utils;

public class User extends Setup {
	public User() throws IOException {
		initConfig();
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void callingLoginAPI(String email, String password) throws ConfigurationException, IOException {
		RestAssured.baseURI = prop.getProperty("BASE_URL");
		UserModel userModel = new UserModel(email, password);
		Response res = given().contentType("application/json").body(userModel).when().post("/user/login").then()
				.assertThat().statusCode(200).extract().response();

		JsonPath jsonpath = res.jsonPath();
		String token = jsonpath.get("token");
		String message = jsonpath.get("message");
		setMessage(message);
		Utils.setEnvVariable("TOKEN", token);
	}

	public String callingUserListAPI() throws IOException {
		RestAssured.baseURI = prop.getProperty("BASE_URL");
		Response res = given().contentType("application/json").header("Authorization", prop.getProperty("TOKEN")).when()
				.get("/user/list").then().assertThat().statusCode(200).extract().response();

		JsonPath response = res.jsonPath();
		String id = response.get("users[0].id").toString();
		return id;
	}
}