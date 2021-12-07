package com.hsbc.stepDefinitions;

import com.hsbc.services.LatestExchangeRatesService;
import com.hsbc.utils.ApiHelper;
import com.hsbc.utils.TestContext;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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

public class LatestExchangeRatesSteps {

    LatestExchangeRatesService latestExchangeRatesService = new LatestExchangeRatesService();

    private Response latestExchangeRateJsonResponse;

    @When("^Customer send request to get latest exchange rate$")
    public void customer_send_request_to_get_latest_exchange_rate() {
        latestExchangeRateJsonResponse = latestExchangeRatesService.getLatestExchangeRate();
        TestContext.getInstance().setXchangeResponse("latest",latestExchangeRateJsonResponse);
        assert latestExchangeRateJsonResponse!= null;
        assertThat("Invalid response from the server",latestExchangeRateJsonResponse.getBody().asString(),matchesJsonSchema(new File("src/test/resources/exchangesratesschema.json")));
    }

    @Then("^latest exchange rates API response should return (\\d+) status code$")
    public void api_should_return_status_response(int expectedResponseCode) {
        int actualResponseCode = latestExchangeRateJsonResponse.getStatusCode();
        switch (expectedResponseCode) {
            case 200:
                expectedResponseCode = HttpStatus.SC_OK;
                break;
            case 400:
                expectedResponseCode = HttpStatus.SC_BAD_REQUEST;
                break;
        }
        assertThat("Response code should be equal to "+expectedResponseCode,actualResponseCode,is(expectedResponseCode));
    }

    @Then("^latest exchange rates API should return all supported currency exchange rates$")
    public void api_should_return_all_supported_currency_exchange_rates() {
        String json = latestExchangeRateJsonResponse.asString();
        Map<String,Double> supportedExchangeRates = from(json).getMap("rates");
        if(from(json).getString("base").equals("EUR")) {
            assertThat("Response body should have all supported exchange rates ", supportedExchangeRates.size(), is(32));
        } else {
            assertThat("Response body should have all supported exchange rates ", supportedExchangeRates.size(), is(33));
        }        for(String key : supportedExchangeRates.keySet()) {
            assertThat("Response body should have all exchange rates greater than zero ",supportedExchangeRates.get(key),is(not(Double.valueOf(0.0))));
        }
    }

    @Then("^latest exchange rates API response should have base currency as \"([^\"]*)\"$")
    public void api_response_should_have_base_currency(String base) {
        String json = latestExchangeRateJsonResponse.asString();
        String baseCurrency = from(json).getString("base");
        assertThat("Response body should have base currency as "+baseCurrency + " in the response",baseCurrency,is(base));
    }

    @Then("^latest exchange rates API response should have date as \"([^\"]*)\"$")
    public void api_response_should_have_date(String expectedDate) {
        if(expectedDate.equalsIgnoreCase("current date")) {
            expectedDate = ApiHelper.getCurrentDate();
        }
        latestExchangeRateJsonResponse.then().body("date",is(expectedDate));
    }

    @When("^Customer send request to get latest exchange rate for following currencies$")
    public void customerSendRequestToGetLatestExchangeRateForGBPAndUSD(List<String> symbols) {
        latestExchangeRateJsonResponse = latestExchangeRatesService.getLatestExchangeRateForSpecificCurrencies(symbols);
        assert latestExchangeRateJsonResponse!= null;
    }

    @And("^latest exchange rates API should return only following currency exchange rates$")
    public void apiShouldReturnOnlyFollowingCurrencyExchangeRates(List<String> expectedCurrencies) {
        String json = latestExchangeRateJsonResponse.asString();
        Map<String,Double> supportedExchangeRates = from(json).getMap("rates");
        assertThat("Response body should have only " + expectedCurrencies.size() +" exchange rates ",supportedExchangeRates.keySet(),hasSize(expectedCurrencies.size()));
        assertThat("Response body should have only requested exchange rates ",supportedExchangeRates.keySet(),containsInAnyOrder(expectedCurrencies.toArray(new String[expectedCurrencies.size()])));
    }

    @When("^Customer send request to get latest exchange rate with base currency as \"([^\"]*)\"$")
    public void customerSendRequestToGetLatestExchangeRateWithBaseCurrencyAs(String base) {
        latestExchangeRateJsonResponse = latestExchangeRatesService.getLatestExchangeRateForSpecificBaseCurrency(base);
        assert latestExchangeRateJsonResponse!= null;
        assertThat("Invalid response from the server",latestExchangeRateJsonResponse.getBody().asString(),matchesJsonSchema(new File("src/test/resources/exchangesratesschema.json")));
    }

    @When("^Customer send request to get latest exchange rate with empty symbols parameter$")
    public void customerSendRequestToGetLatestExchangeRateWithEmptySymbolsParameter() {
        latestExchangeRateJsonResponse = latestExchangeRatesService
            .getLatestExchangeRateForSpecificCurrencies(Arrays.asList(""));
        assert latestExchangeRateJsonResponse!= null;
    }

    @When("^Customer send request to get latest exchange rate with empty base parameter$")
    public void customerSendRequestToGetLatestExchangeRateWithEmptyBaseParameter() {
        latestExchangeRateJsonResponse = latestExchangeRatesService.getLatestExchangeRateForSpecificBaseCurrency("");
        assert latestExchangeRateJsonResponse!= null;
    }

    @When("^Customer send request to get latest exchange rate with specific base currency and symbols$")
    public void customerSendRequestToGetLatestExchangeRateWithSpecificBaseCurrencyAndSymbols(List<Map<String,String>> params) {
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
        latestExchangeRateJsonResponse = latestExchangeRatesService
            .getLatestExchangeRateForSpecificBaseCurrencyAndSymbols(base, symbols);
        assert latestExchangeRateJsonResponse!= null;
    }

    @And("^latest exchange rates API response should contain \"([^\"]*)\"$")
    public void apiShouldReturnFollowingResponse(String expectedResponse) {
        assertThat("Response body should contain error message " +expectedResponse ,latestExchangeRateJsonResponse.getBody().asString(),containsString(expectedResponse));
    }

    @When("^Customer send request to get latest exchange rate with an incomplete URL$")
    public void customerSendRequestToGetLatestExchangeRateWithAnIncompleteURL() {
        latestExchangeRateJsonResponse = latestExchangeRatesService.getLatestExchangeRateWithIncompleteURL();
    }
}
