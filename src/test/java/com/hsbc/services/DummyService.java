package com.hsbc.services;

import static org.hamcrest.MatcherAssert.assertThat;

import com.hsbc.utils.ApiHelper;
import com.hsbc.utils.Routes;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class DummyService {

  public Response getAllUsers() {
    try {
      RequestSpecification requestSpec = ApiHelper.getGenericConfig();
      Response usersResponse = ApiHelper.getRequest(Routes.GET_USERS.getEndpoint(), requestSpec);
      return  usersResponse;
    } catch (Exception e) {
      if("Connection refused: connect".equalsIgnoreCase(e.getLocalizedMessage())) {
        assertThat("Service Unavailable [" + e.getLocalizedMessage() + "]",false);
      }
    }
    return null;
  }

  public Response createNewUser(Map<String, String> params) {
    try {
      RequestSpecification requestSpec = ApiHelper.getGenericConfig();
      params.forEach(requestSpec::queryParam);
      return ApiHelper.postRequest(Routes.CREATE_USER.getEndpoint(), requestSpec);
    } catch (Exception e) {
      if("Connection refused: connect".equalsIgnoreCase(e.getLocalizedMessage())) {
        assertThat("Service Unavailable [" + e.getLocalizedMessage() + "]",false);
      }
    }
    return null;
  }
}
