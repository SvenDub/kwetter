Feature: Home Page

  Background:
    Given The test data set "default" is loaded

  Scenario: Search tweets by content
    When I search for "Hello"
    Then I get 2 tweets containing "Hello"

  Scenario: Search tweets by user
    When I search for "@SvenDub"
    Then I get 2 tweets from "SvenDub"

  Scenario: Place tweet

  Scenario: Show timeline

  Scenario: Mention user in tweet

  Scenario: Show tweets mentioning me

  Scenario: Show popular trends

  Scenario: Mention trend in tweet

  Scenario: Show tweets by trend

