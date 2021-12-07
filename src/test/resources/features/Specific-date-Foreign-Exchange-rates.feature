@specific-date-xchange-rate
Feature: Specific date Foreign Exchange rates
    As a Customer
    I want to be able get different currency exchange rate with specific date
    So that I can use it for financial reasons

    # Scenarios to test with different Dates includes { current date, Past date , Feature date and Least possible date (boundary) }
     Scenario Outline: Customer request exchange rate with specific date
         When Customer send request to get exchange rate with date as "<requestDate>"
         Then specific date exchange rates API response should return <statusCode> status code
         And specific date exchange rates API should return all supported currency exchange rates
         And specific date exchange rates API response should have base currency as "<baseCurrency>"
         And specific date exchange rates API response should have date as "<responseDate>"
         Examples:
             | requestDate  | statusCode | baseCurrency | responseDate |
             | current date | 200        | EUR          | current date |
             | 2000-05-01   | 200        | EUR          | 2000-05-01   |
             | 2022-10-31   | 200        | EUR          | current date |
             | 1999-04-01   | 200        | EUR          | 1999-04-01   |

    Scenario: Customer request exchange rate with older than Least possible date
        When Customer send request to get exchange rate with date as "1998-04-01"
        Then specific date exchange rates API response should return 400 status code
        And specific date exchange rates API response should contain "There is no data for dates older then 1999-01-04."

    Scenario: Customer request exchange rate with invalid date format
        When Customer send request to get exchange rate with date as "2020-Apr-21"
        Then specific date exchange rates API response should return 400 status code
        And specific date exchange rates API response should contain "time data '2020-Apr-21' does not match format '%Y-%m-%d'"

    Scenario: Customer request exchange rate for a specific date with base currency as GBP And Symbols as USD & AUD
        When Customer send request to get exchange rate for date "current date" with following query parameters
            | base | symbols |
            | GBP  | USD,AUD |
        Then specific date exchange rates API response should return 200 status code
        And specific date exchange rates API should return only following currency exchange rates
            | USD |
            | AUD |
        And specific date exchange rates API response should have base currency as "GBP"
        And specific date exchange rates API response should have date as "current date"

    Scenario: Customer request exchange rate for a specific date with Symbols as AUD & CAD
        When Customer send request to get exchange rate for date "current date" with following query parameters
            | symbols |
            | CAD,AUD |
        Then specific date exchange rates API response should return 200 status code
        And specific date exchange rates API should return only following currency exchange rates
            | AUD |
            | CAD |
        And specific date exchange rates API response should have base currency as "EUR"
        And specific date exchange rates API response should have date as "current date"

    Scenario: Customer request exchange rate for a specific date with invalid Symbols
        When Customer send request to get exchange rate for date "current date" with following query parameters
            | symbols |
            | US      |
        Then specific date exchange rates API response should return 400 status code
        And specific date exchange rates API response should contain "Symbols 'US' are invalid for date"

    Scenario: Customer request exchange rate with incomplete URL
        When Customer send request to get exchange rate with an incomplete URL
        Then specific date exchange rates API response should return 400 status code
        And specific date exchange rates API response should contain "time data 'api' does not match format '%Y-%m-%d'"

    Scenario: Customer request exchange rate for a specific date with base & Symbols as same EUR
        When Customer send request to get exchange rate for date "current date" with following query parameters
            | base | symbols |
            | EUR  | EUR     |
        Then specific date exchange rates API response should return 400 status code
        And specific date exchange rates API response should contain "Symbols 'EUR' are invalid for date"
