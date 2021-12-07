package com.hsbc.stepDefinitions;

import com.hsbc.services.LatestExchangeRatesService;
import com.hsbc.services.SpecificDateExchageRateService;
import com.hsbc.utils.ApiHelper;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

public class SpecificDateExchangeRateSteps {

    private SpecificDateExchageRateService specificDateExchageRateService = new SpecificDateExchageRateService();

    private LatestExchangeRatesService latestExchangeRatesService = new LatestExchangeRatesService();

    private Response specificDateJsonResponse;

    @When("^Customer send request to get exchange rate with date as \"([^\"]*)\"$")
    public void customerSendRequestToGetExchangeRateWithDateAs(String date) {
        specificDateJsonResponse = specificDateExchageRateService.getSpecificDateExchangeRate(date);
        assert specificDateJsonResponse!=null;
        if(specificDateJsonResponse.getStatusCode() ==200) {
            assertThat("Invalid response from the server", specificDateJsonResponse.getBody().asString(),
                       matchesJsonSchema(new File("src/test/resources/exchangesratesschema.json")));
        }
    }

    @Then("^specific date exchange rates API response should return (.*) status code$")
    public void specificDateExchangeRatesAPIResponseShouldReturnStatusCode(int expectedStatusCode) {
        int actualResponseCode = specificDateJsonResponse.getStatusCode();
        Integer.valueOf(expectedStatusCode);
        switch (expectedStatusCode) {
            case 200:
                expectedStatusCode = HttpStatus.SC_OK;
                break;
            case 400:
                expectedStatusCode = HttpStatus.SC_BAD_REQUEST;
                break;
        }
        assertThat("Response code should be equal to "+expectedStatusCode,actualResponseCode,is(expectedStatusCode));
    }

    @And("^specific date exchange rates API should return all supported currency exchange rates$")
    public void specificDateExchangeRatesAPIShouldReturnAllSupportedCurrencyExchangeRates() {
        String json = specificDateJsonResponse.asString();
        Map<String,Double> supportedExchangeRates = from(json).getMap("rates");
        if(from(json).getString("base").equals("EUR")) {
            assertThat("Response body should have all supported exchange rates ", supportedExchangeRates.size(), is(32));
        } else {
            assertThat("Response body should have all supported exchange rates ", supportedExchangeRates.size(), is(33));
        }
        for(String key : supportedExchangeRates.keySet()) {
            assertThat("Response body should have all exchange rates greater than zero ",supportedExchangeRates.get(key),is(not(0.0)));
        }
    }

    @And("^specific date exchange rates API response should have base currency as \"([^\"]*)\"$")
    public void specificDateExchangeRatesAPIResponseShouldHaveBaseCurrencyAs(String expectedBase) {
        String json = specificDateJsonResponse.asString();
        String baseCurrency = from(json).getString("base");
        assertThat("Response body should have base currency as "+baseCurrency + "in the response",baseCurrency,is(expectedBase));
    }

    @And("^specific date exchange rates API response should have date as \"([^\"]*)\"$")
    public void specificDateExchangeRatesAPIResponseShouldHaveDateAs(String expectedDate) {
        if(expectedDate.equalsIgnoreCase("current date")) {
            expectedDate = ApiHelper.getCurrentDate();
        }
        specificDateJsonResponse.then().body("date",is(expectedDate));
    }

    @And("^specific date exchange rates API response should contain \"([^\"]*)\"$")
    public void specificDateExchangeRatesAPIResponseShouldContain(String expectedErrorMessage) {
        assertThat("Response body should contain error message " +expectedErrorMessage ,specificDateJsonResponse.getBody().asString(),containsString(expectedErrorMessage));
    }

    @When("^Customer send request to get exchange rate for date \"([^\"]*)\" with following query parameters$")
    public void customerSendRequestToGetExchangeRateForDateWithFollowingQueryParameters(String date, List<Map<String,String>> params) {
        String base = null;
        List<String> symbols = new ArrayList<>();
        for(Map<String,String> param : params) {
            if(param.containsKey("base")) {
                base = param.get("base");
            }
            if(param.containsKey("symbols")) {
                String[] sym = param.get("symbols").split(",");
                for(String s : sym) {
                    symbols.add(s);
                }
            }
        }
        specificDateJsonResponse = specificDateExchageRateService.getSpecificDateExchangeRateWithQueryParams(date,base,symbols);
    }

    @When("^Customer send request to get exchange rate with an incomplete URL$")
    public void customerSendRequestToGetExchangeRateWithAnIncompleteURL() {
        specificDateJsonResponse = specificDateExchageRateService.getSpecificDateExchangeRateWithWrongUrl();
    }

    @And("^API should return all supported currency exchange rates which matches on current date$")
    public void apiShouldReturnAllSupportedCurrencyExchangeRatesWhichMatchesOnCurrentDate() {
        Response latestExchangeRequestJsonResponse = latestExchangeRatesService.getLatestExchangeRate();
        assertThat("Response body should match with latest current date ",specificDateJsonResponse.getBody().asString(),is(
            latestExchangeRequestJsonResponse.getBody().asString()));
    }

    @And("^specific date exchange rates API should return only following currency exchange rates$")
    public void specificDateExchangeRatesAPIShouldReturnOnlyFollowingCurrencyExchangeRates(List<String> expectedCurrencies) {
        String json = specificDateJsonResponse.asString();
        Map<String,Double> supportedExchangeRates = from(json).getMap("rates");
        assertThat("Response body should have only " + expectedCurrencies.size() +" exchange rates ",supportedExchangeRates.keySet(),hasSize(expectedCurrencies.size()));
        assertThat("Response body should have only requested exchange rates ",supportedExchangeRates.keySet(),containsInAnyOrder(expectedCurrencies.toArray(new String[expectedCurrencies.size()])));
    }
}
