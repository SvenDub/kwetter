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
    Given I am logged in as "<user>" with password "<password>"
    When I place a tweet containing "<content>"
    Then The server responds with 201 and redirects me to "/tweets/(\d+)"
    And A tweet containing "<content>" from "<user>" should exist
    Examples:
      | user             | password | content                   |
      | SvenDub          | password | My very first tweet.      |
      | DeEnigeEchteSven | password | How is it going @SvenDub? |

  Scenario: Show timeline
    Given I am logged in as "DeEnigeEchteSven" with password "password"
    When I load my timeline
    Then I get 3 tweets

  Scenario: Show timeline
    Given I am logged in as "SvenDub" with password "password"
    When I load my timeline
    Then I get 2 tweets

  Scenario: Mention user in tweet
    Given I am logged in as "SvenDub" with password "password"
    When I place a tweet containing "How is it going @DeEnigeEchteSven @SvenDub?"
    Then A tweet containing "How is it going @DeEnigeEchteSven @SvenDub?" from "SvenDub" should exist
    And A tweet mentioning "DeEnigeEchteSven" should exist
    And A tweet mentioning "SvenDub" should exist

  Scenario: Show tweets mentioning me
    Given I am logged in as "SvenDub" with password "password"
    And I place a tweet containing "How is it going @DeEnigeEchteSven?"
    And I place a tweet containing "It's a me, @SvenDub!"
    When I load my mentions
    Then I get 1 tweets

  Scenario: Show popular trends
    Given I am logged in as "SvenDub" with password "password"
    And I place a tweet containing "Coding and coding #javaee #fontys"
    And I place a tweet containing "Building kwetter in #javaee"
    When I load popular trends
    Then I get 2 times hashtag "#javaee"
    And I get 1 times hashtag "#fontys"


  Scenario: Mention trend in tweet
    Given I am logged in as "SvenDub" with password "password"
    When I place a tweet containing "Coding and coding #javaee #fontys"
    Then A tweet containing "Coding and coding #javaee #fontys" from "SvenDub" should exist
    And A tweet with hashtag "#javaee" should exist
    And A tweet with hashtag "#fontys" should exist


  Scenario: Show tweets by trend
    Given I am logged in as "SvenDub" with password "password"
    And I place a tweet containing "Coding and coding #javaee #fontys"
    And I place a tweet containing "Building kwetter in #javaee"
    When I search for "#javaee"
    Then I get 2 tweets containing "#javaee"

