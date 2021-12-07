Feature: Latest Foreign Exchange rates
    As a Customer
    I want to be able get latest different currency exchange rate
    So that I can use it for financial reasons

    Background:
        Given The Rates API services up and running

    Scenario: Customer request latest exchange rate
        When Customer send request to get latest exchange rate
        Then API should return 200 status response
        And API should return all supported currency exchange rates
        And API response should have base currency as EUR
        And API response should have date as current date

    Scenario: Customer request latest exchange rate for specific Currencies
        When Customer send request to get latest exchange rate for GBP and USD
        Then API should return 200 status response
        And API should return only currency exchange rates for GBP and USD
        And API response should have base currency as EUR
        And API response should have date as current date

    Scenario: Customer request latest exchange rate with base currency as GBP
        When Customer send request to get latest exchange rate with base currency as GBP
        Then API should return 200 status response
        And API should return all supported currency exchange rates
        And API response should have base currency as GBP
        And API response should have date as current date

    Scenario: Customer request latest exchange rate with base currency as GBP And Symbols as USD & AUD
        When Customer send request to get latest exchange rate with base currency as GBP
        And Symbols as USD and AUD
        Then API should return 200 status response
        And API should return only currency exchange rates for USD and AUD
        And API response should have base currency as GBP
        And API response should have date as current date

    Scenario: Customer request latest exchange rate with empty symbols parameter
        When Customer send request to get latest exchange rate with empty symbols parameter
        Then API should return 200 status response
        And API should return all supported currency exchange rates
        And API response should have base currency as EUR
        And API response should have date as current date

    Scenario: Customer request latest exchange rate with empty base parameter
        When Customer send request to get latest exchange rate with empty base parameter
        Then API should return 200 status response
        And API should return all supported currency exchange rates
        And API response should have base currency as EUR
        And API response should have date as current date

    Scenario: Customer request latest exchange rate with bothi base and symbols parameter empty
        When Customer send request to get latest exchange rate with empty base parameter
        And Empty Symbols parameter
        Then API should return 200 status response
        And API should return all supported currency exchange rates
        And API response should have base currency as EUR
        And API response should have date as current date

    Scenario: Customer request latest exchange rate with incomplete URL
        When Customer send request to get exchange rate with an incomplete URL
        Then API response should return 400 status code
        And API response should contain "time data 'api' does not match format '%Y-%m-%d'"

    Scenario: Customer request latest exchange rate with invalid Symbols
        When Customer send request to get latest exchange rate for GB
        Then latest exchange rates API response should return 400 status code
        And latest exchange rates API response should contain "Symbols 'GB' are invalid for date"
