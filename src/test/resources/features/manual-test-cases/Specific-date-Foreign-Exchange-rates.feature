Feature: Specific date Foreign Exchange rates
    As a Customer
    I want to be able get different currency exchange rate with specific date
    So that I can use it for financial reasons

    Background:
        Given The Rates API services up and running

    Scenario: Customer request exchange rate with specific date
        When Customer send request to get exchange rate with date as "2019-04-23"
        Then API should return 200 status response
        And API should return all supported currency exchange rates
        And API response should have base currency as EUR
        And API response should have date as "2019-04-21"

    Scenario: Customer request exchange rate with Past date
        When Customer send request to get exchange rate with date as "2000-05-01"
        Then API should return 200 status response
        And API should return all supported currency exchange rates
        And API response should have base currency as EUR
        And API response should have date as "2000-05-01"

    Scenario: Customer request exchange rate with Feature date
        When Customer send request to get exchange rate with date as "2022-10-31"
        Then API should return 200 status response
        And API should return all supported currency exchange rates which matches on current date response
        And API response should have base currency as EUR
        And API response should have date as current date

    Scenario: Customer request exchange rate with Least possible date (boundary)
        When Customer send request to get exchange rate with date as "1999-04-01"
        Then API should return 200 status response
        And API should return all supported currency exchange rates
        And API response should have base currency as EUR
        And API response should have date as "1999-04-01"

    Scenario: Customer request exchange rate for a specific date with base currency as GBP And Symbols as USD & AUD
        When Customer send request to get exchange rate for "2019-04-21" with base currency as GBP
        And Symbols as USD and AUD
        Then API should return 200 status response
        And API should return only currency exchange rates for USD and AUD
        And API response should have base currency as GBP
        And API response should have date as "2019-04-21"

    Scenario: Customer request exchange rate for a specific date with Symbols as AUD & CAD
        When Customer send request to get exchange rate for "2019-04-21"
        And Symbols as USD and AUD
        Then API should return 200 status response
        And API should return only currency exchange rates for USD and AUD
        And API response should have base currency as EUR
        And API response should have date as "2019-04-21"

    Scenario: Customer request exchange rate with older than Least possible date
        When Customer send request to get exchange rate with date as "1998-04-01"
        Then API should return 400 status response
        And API should return following response
        |"error": "There is no data for dates older then 1999-01-04."|

    Scenario: Customer request exchange rate with invalid date format
        When Customer send request to get exchange rate with date as "2020-Apr-21"
        Then API should return 400 status response
        And API should return following response
        |"error": "time data '2020-Apr-21' does not match format '%Y-%m-%d'"|

    Scenario: Customer request exchange rate for a specific date with empty base and symbols parameter
        When Customer send request to get exchange rate for "2019-04-21" with empty base parameter
        And Empty Symbols parameter
        Then API should return 200 status response
        And API should return all supported currency exchange rates
        And API response should have base currency as EUR
        And API response should have date as "2019-04-21"

    Scenario: Customer request exchange rate for a specific date with invalid Symbols
        When Customer send request to get exchange rate for "2019-04-21"
        And Symbols as US
        Then API should return 400 status response
        And API should return following response
            |"error": "Symbols 'US' are invalid for date 2020-04-21."|

    Scenario: Customer request exchange rate with incomplete URL
        When Customer send request to get exchange rate with an incomplete URL
        Then API should return 400 status response
        And API should return following response
            |"error": "time data 'api' does not match format '%Y-%m-%d'"|

    Scenario: Customer request exchange rate for a specific date which falls on Week-end or Bank holiday
        When Customer send request to get exchange rate for date "2020-04-18"
        Then API should return 200 status response
        And API response should match with the previous working day "2020-04-17"

