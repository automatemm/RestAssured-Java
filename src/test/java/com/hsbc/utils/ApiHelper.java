package com.hsbc.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiHelper {

    public static RequestSpecification getGenericConfig() {
        RestAssured.useRelaxedHTTPSValidation();
        RequestSpecification reqSpec = given().urlEncodingEnabled(false).contentType(ContentType.JSON);
        reqSpec.log().all();
        return reqSpec;
    }

    public static Response getRequest(String contextPath, RequestSpecification reqSpec) {
        return logResponse(reqSpec.get(contextPath));
    }

    public static Response postRequest(String contextPath, RequestSpecification reqSpec) {
        return logResponse(reqSpec.post(contextPath));
    }

    private static Response logResponse(Response response) {
        return response.then().log().all().extract().response();
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
