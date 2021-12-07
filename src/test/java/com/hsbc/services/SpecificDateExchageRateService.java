package com.hsbc.services;

import com.hsbc.utils.ApiHelper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class SpecificDateExchageRateService {

    public Response getSpecificDateExchangeRate(String date) {
        if(date.equalsIgnoreCase("current date")) {
            date = ApiHelper.getCurrentDate();
        }
        try {
            RequestSpecification reqSpec = ApiHelper.getGenericConfig();
            return ApiHelper.getRequest(date, reqSpec);
        } catch (Exception e) {
            if("Connection refused: connect".equalsIgnoreCase(e.getLocalizedMessage())) {
                assertThat("Service Unavailable [" + e.getLocalizedMessage() + "]",false);
            }
        }
        return null;
    }

    public Response getSpecificDateExchangeRateWithQueryParams(String date, String base, List<String> symbols) {
        try {
            if(date.equalsIgnoreCase("current date")) {
                date = ApiHelper.getCurrentDate();
            }
            RequestSpecification reqSpec = ApiHelper.getGenericConfig();
            reqSpec.param("base",base);
            reqSpec.param("symbols",symbols);
            return ApiHelper.getRequest(date, reqSpec);
        } catch (Exception e) {
            if("Connection refused: connect".equalsIgnoreCase(e.getLocalizedMessage())) {
                assertThat("Service Unavailable [" + e.getLocalizedMessage() + "]",false);
            }
        }
        return null;
    }

    public Response getSpecificDateExchangeRateWithWrongUrl() {
        try {
            RequestSpecification reqSpec = ApiHelper.getGenericConfig();
            return ApiHelper.getRequest("", reqSpec);
        } catch (Exception e) {
            if("Connection refused: connect".equalsIgnoreCase(e.getLocalizedMessage())) {
                assertThat("Service Unavailable [" + e.getLocalizedMessage() + "]",false);
            }
        }
        return null;
    }
}
