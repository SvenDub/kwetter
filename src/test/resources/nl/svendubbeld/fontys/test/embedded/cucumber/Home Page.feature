Feature: Home Page

  Background:
    Given The test data set "default" is loaded
    And The REST client is configured to use "/api"

  Scenario: Search tweets by content
    When I search for "Hello"
    Then I get 2 tweets containing "Hello"

  Scenario: Search tweets by user
    When I search for "@SvenDub"
    Then I get 2 tweets from "SvenDub"

  Scenario Outline: Place tweet
    Given I am logged in as "<user>"
    When I place a tweet containing "<content>"
    Then The server responds with 201 and redirects me to "/tweets/(\d+)"
    And A tweet containing "<content>" from "<user>" should exist
    Examples:
      | user             | content                   |
      | SvenDub          | My very first tweet.      |
      | DeEnigeEchteSven | How is it going @SvenDub? |

  Scenario: Show timeline
    Given I am logged in as "DeEnigeEchteSven"
    When I load my timeline
    Then I get 3 tweets

  Scenario: Show timeline
    Given I am logged in as "SvenDub"
    When I load my timeline
    Then I get 2 tweets

  Scenario: Mention user in tweet

  Scenario: Show tweets mentioning me

  Scenario: Show popular trends

  Scenario: Mention trend in tweet

  Scenario: Show tweets by trend

