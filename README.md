# Hsbc Technical Test for API Automation

# All test scenarios developed by following documentation in the Given URL
https://reqres.in/
https://ratesapi.io/documentation/

# to generate schema
https://jsonschema.net/home

## Notes about the Test Framework

This Test Automation Framework is built with Following tech stack:
* Java
* RestAssured
* Cucumber
* Junit
* Maven
* Hamcrest Assertion Library

### How to run the tests

* Clone the repository locally (git clone https://github.com/automatemm/RestAssured-Java.git)
* Create a Junit Runner with following VM options
* Runner Class to be selected is : com.hsbc.RunApiTests
    `-Dcucumber.options="--tags @mk"`
 All possible scenarios automated, and there are some duplicate scenarios covered for both Latest and specific dates API.     
 JsonSchema generated from the latest API response and used as base to validate JsonSchema. 
 JsonSchema is available in the path  /src/test/resources/exchangesratesschema.json
 
## Test Results 

After the end of test execution, html report stored in the folder, /Users/bothi/hsbc-technical-test/target/cucumber-report/apiTests/index.html
it would display outcome of all the scenarios.
 

### Manual Test case Scenarios
All possible scenarios for both Latest and specific date exchange rates API stored in the following path
/src/test/resources/features/manual-test-cases

### Observation and Assumption
* Assumption 1 : if the specific date falls on Weekend and Bank- holiday then previous working day's Exchange rates ar returned. 

* Assumption 2 : When the API request sent to get the latest exchange rates, not always the current date is returned ( need more businees input)

* Observation 1 : When base currency & symbol query used as "EUR" the 400 response returned where as for all other
                currencies it is 200 response.
                
* Observation 2 : When specific dates used in the past (ex. year 2000), not all of the exchange rates returned,
                assumed some of those currencies not part of the supported list.
                
* Since there are no defined expected results available for exchange rates & also it is dynamic,specific values are not asserted.


