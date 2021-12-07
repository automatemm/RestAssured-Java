package com.hsbc.stepDefinitions;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.hsbc.services.DummyService;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpStatus;

public class DummySteps {

  DummyService dummyService = new DummyService();

  private Response latestPostResponse;

  private Response createUserJSONResponse;

  @When("^Customer send request to get all users and validate$")
  public void customer_send_request_to_get_all_users() {
    latestPostResponse = dummyService.getAllUsers();
    assert latestPostResponse != null;
    assertThat("Response code should be equal to " + HttpStatus.SC_OK,
        latestPostResponse.getStatusCode(), is(HttpStatus.SC_OK));
    assertThat("Invalid response from the server",latestPostResponse.getBody().asString(),matchesJsonSchema(new File("src/test/resources/allUsersSchema.json")));
  }

  @When("^I create a New User and validate$")
  public void iCreateANewUser() {
    Map<String, String> params = new HashMap<>();
    params.put("name","maddy");
    params.put("job","dev");
    createUserJSONResponse = dummyService.createNewUser(params);
    String json = createUserJSONResponse.asString();
    String id = from(json).getString("createdAt");
    assert createUserJSONResponse != null;
    assertThat("Response code should be equal to " + HttpStatus.SC_OK,
        createUserJSONResponse.getStatusCode(), is(HttpStatus.SC_CREATED));
    assertThat("Id is null", id, is(notNullValue()));
    assertThat("Invalid response from the server",createUserJSONResponse.getBody().asString(),matchesJsonSchema(new File("src/test/resources/createUserSchema.json")));
  }
}
