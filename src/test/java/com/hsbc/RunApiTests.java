package com.hsbc;


import com.hsbc.utils.TestContext;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true,
    tags = {"@mk"},
    features ="src/test/resources/features",
    plugin = {"pretty","html:target/cucumber-report/apiTests",
              "json:target/cucumber-report/apiTests/cucumber.json"},
    glue = {"com.hsbc.stepDefinitions"} )


public class RunApiTests {

    @BeforeClass
    public static void setUp() {
        TestContext testContext  = TestContext.getInstance();
        RestAssured.baseURI = testContext.getKey("base.url");
    }
}

