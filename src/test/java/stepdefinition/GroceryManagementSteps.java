package stepdefinition;

import io.cucumber.java.en.*;
import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
public class GroceryManagementSteps {
	private Response response;
	private String baseUrl = "http://localhost:3001";

	@Given("the API is running")
	public void the_api_is_running() {
		//setting up baseURI
		baseURI = baseUrl;
	}
    //User registration
	@When("I register a new user with username {string} and email {string}")
    public void i_register_a_new_user_with_username_and_email(String username, String email) {
    String body = "{ \"username\": \"" + username + "\", \"password\": \"Password123\", \"email\":\"" + email + "\" }";
     response =
       given()
       .header("Content-Type", "application/json")
       .body(body)
       .when()
       .post("/users/register");
    }

	@Then("the user should be successfully registered with status code {int}")
	public void the_user_should_be_successfully_registered_with_status_code(Integer statusCode) {
		//assert the status code
		Assert.assertEquals(statusCode.intValue(), response.getStatusCode());
	}
    
	//User login
	@When("I log in with username {string} and password {string}")
	public void i_log_in_with_username_and_password(String username, String password) {
		String body = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";
		response = given().header("Content-Type", "application/json").body(body).when().post("/users/login");
	}

	@Then("the login should be successful with status code {int}")
	public void the_login_should_be_successful_with_status_code(Integer statusCode) {
		//assert the actual status code with the expected
		Assert.assertEquals(statusCode.intValue(), response.getStatusCode());
	}
	
	//Add a new product
	@When("I add a product with name {string}, category {string}, price {double}, and stock quantity {int}")
	public void i_add_a_product_with_name_category_price_and_stock_quantity(String name, String category, Double price,
			Integer stockQuantity) {
		String body = "{ \"name\": \"" + name + "\", \"category\": \"" + category + "\", \"price\": " + price
				+ ", \"stockQuantity\": " + stockQuantity + " }";
		response = given()
				.header("Content-Type", "application/json")
				.body(body)
				.when()
				.post("/products");
	}

	@Then("the product should be successfully added with status code {int}")
	public void the_product_should_be_successfully_added_with_status_code(Integer statusCode) {
		//assert the actual status code with expected
		Assert.assertEquals(statusCode.intValue(), response.getStatusCode());
	}
	
	//Retrieve product details
	@When("I send the get the request for product details")
	public void I_send_the_get_the_request_for_product_details() {
		response = given()
				.header("Content-Type", "application/json")
				.when()
				.get("/products");
	}

	@Then("the response status code for this get request should be {int}")
	public void the_response_status_code_for_this_get_request_should_be_(Integer int1) {
		//assert the status code
		Assert.assertEquals(int1.intValue(), response.getStatusCode());
	}
    
	//Search products by name
	@When("I search for products with name {string}")
	public void i_search_for_products_with_name(String name) {
		response = given().queryParam("name", name).when().get("/products/search");
	}

	@Then("I should get the product details with status code {int}")
	public void i_should_get_the_product_details_with_status_code(Integer statusCode) {
		//validating the status code
		Assert.assertEquals(statusCode.intValue(), response.getStatusCode());
	}
    
	//Place a new order
	@When("I place an order with userId {int}, productId {int}, quantity {int}, and totalPrice {double}")
	public void i_place_an_order_with_userId_productId_quantity_and_totalPrice(Integer userId, Integer productId,
			Integer quantity, Double totalPrice) {
		String body = "{ \"userId\": " + userId + ", \"productId\": " + productId + ", \"quantity\": " + quantity
				+ ", \"totalPrice\": " + totalPrice + " }";
		response = given().header("Content-Type", "application/json").body(body).when().post("/orders");
	}

	@Then("the order should be successfully created with status code {int}")
	public void the_order_should_be_successfully_created_with_status_code(Integer expectedStatusCode) {
		// Assert that the status code from the response matches the expected status
		// code
		Assert.assertEquals(expectedStatusCode.intValue(), response.getStatusCode());
	}
	
	//Retrieve order details
	@When("I send the get the request for the order details")
	public void I_send_the_get_the_request_for_the_order_details() {
		response = given()
				.header("Content-Type", "application/json")
				.when()
				.get("/orders");
	}

	@Then("the response status code for this request should be {int}")
	public void the_response_status_code_for_this_request_should_be(Integer int2) {
		//assert the status code
		Assert.assertEquals(int2.intValue(), response.getStatusCode());
	}
	
	//Update user profile
	@When("I post the request for updating a profile with username {string} and email {string}")
    public void I_post_the_request_for_updating_a_profile_with_username_and_email(String username, String email) {
    String body = "{ \"username\": \"" + username + "\", \"password\": \"bobpassword\", \"email\":\"" + email + "\" }";
	     response =
	       given()
	       .header("Content-Type", "application/json")
	       .body(body)
	       .when()
	       .put("/users/4");
	}
	
	@Then("the response status code should be {int}")
	public void the_response_status_code_should_be(Integer status) {
		//validating status code
		Assert.assertEquals(status.intValue(), response.getStatusCode());
	}
    
	//Delete user account
	@When("I delete the user with ID {int}")
	public void i_delete_the_user_with_ID(Integer userId) {
		response = RestAssured.given().when().delete("/users/" + userId);
	}

	@Then("the user should be successfully deleted with status code {int}")
	public void the_user_should_be_successfully_deleted_with_status_code(Integer expectedStatusCode) {
		//checking and validating the status code for delete request
		Assert.assertEquals(expectedStatusCode.intValue(), response.getStatusCode());
	}
    
	//Filter products by category
	@When("I filter the products by category {string}")
	public void i_filter_the_products_by_category(String category) {
		response = given().queryParam("category", category).when().get("/products/filter");
	}

	@Then("I should receive a list of products in the {string} category with status code {int}")
	public void i_should_receive_a_list_of_products_in_the_category_with_status_code(String category,
			Integer expectedStatusCode) {
		// Assert the status code
		Assert.assertEquals(expectedStatusCode.intValue(), response.getStatusCode());
		// Check that all returned products belong to the specified category
		response.jsonPath().getList("category").forEach(cat -> {
			Assert.assertEquals(category, cat);
		});
	}
}