package testrunner;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import controller.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import setup.Setup;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTestRunner extends Setup {
	@Test
	public void a_doLogin() throws ConfigurationException, IOException {
		User user = new User();
		user.callingLoginAPI("salman@grr.la", "1234");
		String messageExpected = "Login successfully";
		Assert.assertEquals(user.getMessage(), messageExpected);
	}

	@Test
	public void b_getUserList() throws IOException {
		User user = new User();
		String id = user.callingUserListAPI();
		System.out.println(id);
		Assert.assertEquals(id, String.valueOf(80));
	}

	@Test
	public void c_createNewCustomer() {
		RestAssured.baseURI = "http://dmoney.professionaltrainingbd.com";
		String new_customer_payload = "{\r\n" + "    \"name\":\"zadid_test150\",\r\n"
				+ "    \"email\":\"zadid_test150@customer.com\",\r\n" + "    \"password\":\"123456\",\r\n"
				+ "    \"phone_number\":\"+01234567895-150\",\r\n" + "    \"nid\":\"123456789\",\r\n"
				+ "    \"role\":\"Customer\"\r\n" + "}";

		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Authorization", prop.getProperty("TOKEN"));
		httpRequest.header("X-AUTH-SECRET-KEY", "ROADTOSDET");
		Response response = httpRequest.body(new_customer_payload).post("/user/create");
		int statusCode = response.getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode, 201);
	}

	@Test
	public void d_createNewAgent() {
		RestAssured.baseURI = "http://dmoney.professionaltrainingbd.com";
		String new_agent_payload = "{\r\n" + "    \"name\":\"zadid_test150\",\r\n"
				+ "    \"email\":\"zadid_test150@agent.com\",\r\n" + "    \"password\":\"123456\",\r\n"
				+ "    \"phone_number\":\"+98765432105-150\",\r\n" + "    \"nid\":\"123456789\",\r\n"
				+ "    \"role\":\"Agent\"\r\n" + "}";

		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Authorization", prop.getProperty("TOKEN"));
		httpRequest.header("X-AUTH-SECRET-KEY", "ROADTOSDET");
		Response response = httpRequest.body(new_agent_payload).post("/user/create");
		int statusCode = response.getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode, 201);
	}

	@Test
	public void e_loginWithTheNewCustomer() {
		RestAssured.baseURI = "http://dmoney.professionaltrainingbd.com";
		String new_customer_login_creds = "{\r\n" + "    \"email\":\"zadid_test150@customer.com\",\r\n"
				+ "    \"password\":\"123456\"\r\n" + "}";
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Authorization", prop.getProperty("TOKEN"));
		httpRequest.header("X-AUTH-SECRET-KEY", "ROADTOSDET");
		Response response = httpRequest.body(new_customer_login_creds).post("/user/login");
		int statusCode = response.getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode, 200);
	}

	@Test
	public void f_depositToAgentFromSystem() {
		RestAssured.baseURI = "http://dmoney.professionaltrainingbd.com";
		String deposit_to_agent_payload = "{\r\n" + "		    \"from_account\":\"SYSTEM\",\r\n"
				+ "		    \"to_account\":\"+98765432105-150\",\r\n" + "		    \"amount\":2000\r\n" + "		}";
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Authorization", prop.getProperty("TOKEN"));
		httpRequest.header("X-AUTH-SECRET-KEY", "ROADTOSDET");
		Response response = httpRequest.body(deposit_to_agent_payload).post("/transaction/deposit");
		int statusCode = response.getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode, 201);
		ResponseBody body = response.getBody();
		System.out.println(body.asPrettyString());

	}

	@Test
	public void g_cashInToCustomerFromAgent() {
		RestAssured.baseURI = "http://dmoney.professionaltrainingbd.com";
		String agent_to_customer_payload = "{\r\n" + "    \"from_account\":\"+98765432105-150\",\r\n"
				+ "    \"to_account\":\"+01234567895-150\",\r\n" + "    \"amount\":1000\r\n" + "}";
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Authorization", prop.getProperty("TOKEN"));
		httpRequest.header("X-AUTH-SECRET-KEY", "ROADTOSDET");
		Response response = httpRequest.body(agent_to_customer_payload).post("/transaction/deposit");
		int statusCode = response.getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode, 201);
		ResponseBody body = response.getBody();
		System.out.println(body.asPrettyString());
	}

	@Test
	public void h_checkCustomerBalance() {
		RestAssured.baseURI = "http://dmoney.professionaltrainingbd.com";
		Response res = given().contentType("application/json").header("Authorization", prop.getProperty("TOKEN"))
				.header("X-AUTH-SECRET-KEY", "ROADTOSDET").when().get("/transaction/balance/+01234567895-150").then()
				.assertThat().statusCode(200).extract().response();
		int statusCode = res.getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode, 200);
		ResponseBody body = res.getBody();
		System.out.println(body.asString());
	}

	@Test
	public void i_checkAgentBalance() {
		RestAssured.baseURI = "http://dmoney.professionaltrainingbd.com";
		Response res = given().contentType("application/json").header("Authorization", prop.getProperty("TOKEN"))
				.header("X-AUTH-SECRET-KEY", "ROADTOSDET").when().get("/transaction/balance/+98765432105-150").then()
				.assertThat().statusCode(200).extract().response();
		int statusCode = res.getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode, 200);
		ResponseBody body = res.getBody();
		System.out.println(body.asString());
	}

	@Test
	public void j_cashoutFromCustomer() {
		RestAssured.baseURI = "http://dmoney.professionaltrainingbd.com";
		String customer_money_withdraw_payload = "{\r\n" + "    \"from_account\":\"+98765432105-150\",\r\n"
				+ "    \"to_account\":\"+01234567895-150\",\r\n" + "    \"amount\":500\r\n" + "}";
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");
		httpRequest.header("Authorization", prop.getProperty("TOKEN"));
		httpRequest.header("X-AUTH-SECRET-KEY", "ROADTOSDET");
		Response response = httpRequest.body(customer_money_withdraw_payload).post("/transaction/withdraw");
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
	}

	@Test
	public void k_checkCustomerBalanceAgain() {
		RestAssured.baseURI = "http://dmoney.professionaltrainingbd.com";
		Response res = given().contentType("application/json").header("Authorization", prop.getProperty("TOKEN"))
				.header("X-AUTH-SECRET-KEY", "ROADTOSDET").when().get("/transaction/balance/+01234567895-150").then()
				.assertThat().statusCode(200).extract().response();
		int statusCode = res.getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode, 200);
		ResponseBody body = res.getBody();
		System.out.println(body.asString());
	}

}