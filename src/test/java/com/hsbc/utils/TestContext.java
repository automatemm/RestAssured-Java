package com.hsbc.utils;

import io.restassured.response.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestContext {

    Properties prop = new Properties();

    Map<String, Response> xchangeResponse = new HashMap<>();

    InputStream inputStream = null;

    private static TestContext propertyLoader = new TestContext();

    public TestContext(){
        loadprop();
    }

    public void loadprop() {
        try {
            inputStream = new FileInputStream("src/test/resources/project.properties");
            prop.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Problem with reading Project propertie File");
        }
    }

    public String  getKey(String key){
        return prop.getProperty(key);
    }

    public static TestContext getInstance() {
        return propertyLoader;
    }

    public void setXchangeResponse(String name,Response response) {
        xchangeResponse.put(name,response);
    }

    public Map<String,Response> getXchangeResponse() {
        return xchangeResponse;
    }
}
