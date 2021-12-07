@latest-exchange-rate
Feature: Latest Foreign Exchange rates
    As a Customer
    I want to be able get latest different currency exchange rate
    So that I can use it for financial reasons

    Scenario: Customer request latest exchange rate
        When Customer send request to get latest exchange rate
        Then latest exchange rates API response should return 200 status code
        And latest exchange rates API should return all supported currency exchange rates
        And latest exchange rates API response should have base currency as "EUR"
        And latest exchange rates API response should have date as "current date"

    Scenario: Customer request latest exchange rate for specific Currencies
        When Customer send request to get latest exchange rate for following currencies
            | GBP |
            | USD |
        Then latest exchange rates API response should return 200 status code
        And latest exchange rates API should return only following currency exchange rates
            | GBP |
            | USD |
        And latest exchange rates API response should have base currency as "EUR"
        And latest exchange rates API response should have date as "current date"

    Scenario: Customer request latest exchange rate with base currency as GBP
        When Customer send request to get latest exchange rate with base currency as "GBP"
        Then latest exchange rates API response should return 200 status code
        And latest exchange rates API should return all supported currency exchange rates
        And latest exchange rates API response should have base currency as "GBP"
        And latest exchange rates API response should have date as "current date"

    Scenario: Customer request latest exchange rate with base currency as GBP And Symbols as USD & AUD
        When Customer send request to get latest exchange rate with specific base currency and symbols
            | base | symbols |
            | GBP  | USD,AUD |
        Then latest exchange rates API response should return 200 status code
        And latest exchange rates API should return only following currency exchange rates
            | USD |
            | AUD |
        And latest exchange rates API response should have base currency as "GBP"
        And latest exchange rates API response should have date as "current date"

    Scenario: Customer request latest exchange rate with only empty symbols parameter
        When Customer send request to get latest exchange rate with base currency as "GBP"
        And Customer send request to get latest exchange rate with empty symbols parameter
        Then latest exchange rates API response should return 200 status code
        And latest exchange rates API should return all supported currency exchange rates
        And latest exchange rates API response should have base currency as "GBP"
        And latest exchange rates API response should have date as "current date"

    Scenario: Customer request latest exchange rate with only empty base parameter
        When Customer send request to get latest exchange rate with empty base parameter
        And Customer send request to get latest exchange rate for following currencies
            | GBP |
            | USD |
        Then latest exchange rates API response should return 200 status code
        And latest exchange rates API should return only following currency exchange rates
            | GBP |
            | USD |
        And latest exchange rates API response should have base currency as "EUR"
        And latest exchange rates API response should have date as "current date"

    Scenario: Customer request latest exchange rate with empty base and symbols parameter
        When Customer send request to get latest exchange rate with specific base currency and symbols
            | base | symbols |
            |      |         |
        Then latest exchange rates API response should return 200 status code
        And latest exchange rates API should return all supported currency exchange rates
        And latest exchange rates API response should have base currency as "EUR"
        And latest exchange rates API response should have date as "current date"

    Scenario: Customer request latest exchange rate with invalid Symbols
        When Customer send request to get latest exchange rate for following currencies
            | GB |
        Then latest exchange rates API response should return 400 status code
        And latest exchange rates API response should contain "Symbols 'GB' are invalid for date"

    Scenario: Customer request exchange rate with incomplete URL
        When Customer send request to get latest exchange rate with an incomplete URL
        Then latest exchange rates API response should return 400 status code
        And latest exchange rates API response should contain "time data 'api' does not match format '%Y-%m-%d'"
