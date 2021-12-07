package com.hsbc.services;

import com.hsbc.utils.ApiHelper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class LatestExchangeRatesService {

    public Response getLatestExchangeRate() {
        try {
            RequestSpecification reqSpec = ApiHelper.getGenericConfig();
            Response latestExchangeResponse = ApiHelper.getRequest("/latest",reqSpec);
            return latestExchangeResponse;
        } catch (Exception e) {
            if("Connection refused: connect".equalsIgnoreCase(e.getLocalizedMessage())) {
                assertThat("Service Unavailable [" + e.getLocalizedMessage() + "]",false);
            }
        }
        return null;
    }

    public Response getLatestExchangeRateForSpecificCurrencies(List<String> params) {
        try {
            RequestSpecification reqSpec = ApiHelper.getGenericConfig();
            reqSpec.param("symbols",params);
            Response latestExchangeResponse = ApiHelper.getRequest("/latest",reqSpec);
            return latestExchangeResponse;
        } catch (Exception e) {
            if("Connection refused: connect".equalsIgnoreCase(e.getLocalizedMessage())) {
                assertThat("Service Unavailable [" + e.getLocalizedMessage() + "]",false);
            }
        }
        return null;
    }

    public Response getLatestExchangeRateForSpecificBaseCurrency(String base) {
        try {
            RequestSpecification reqSpec = ApiHelper.getGenericConfig();
            reqSpec.param("base",base);
            Response latestExchangeResponse = ApiHelper.getRequest("/latest",reqSpec);
            return latestExchangeResponse;
        } catch (Exception e) {
            if("Connection refused: connect".equalsIgnoreCase(e.getLocalizedMessage())) {
                assertThat("Service Unavailable [" + e.getLocalizedMessage() + "]",false);
            }
        }
        return null;
    }

    public Response getLatestExchangeRateForSpecificBaseCurrencyAndSymbols(String base,List<String> symbols) {
        try {
            RequestSpecification reqSpec = ApiHelper.getGenericConfig();
            reqSpec.param("base",base);
            reqSpec.param("symbols",symbols);
            Response latestExchangeResponse = ApiHelper.getRequest("/latest",reqSpec);
            return latestExchangeResponse;
        } catch (Exception e) {
            if("Connection refused: connect".equalsIgnoreCase(e.getLocalizedMessage())) {
                assertThat("Service Unavailable [" + e.getLocalizedMessage() + "]",false);
            }
        }
        return null;
    }

    public Response getLatestExchangeRateWithIncompleteURL() {
        try {
            RequestSpecification reqSpec = ApiHelper.getGenericConfig();
            Response latestExchangeResponse = ApiHelper.getRequest("",reqSpec);
            return latestExchangeResponse;
        } catch (Exception e) {
            if("Connection refused: connect".equalsIgnoreCase(e.getLocalizedMessage())) {
                assertThat("Service Unavailable [" + e.getLocalizedMessage() + "]",false);
            }
        }
        return null;
    }
}
