package nl.svendubbeld.fontys.test.embedded.cucumber;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import nl.svendubbeld.fontys.dto.TweetDTO;
import org.apache.http.HttpStatus;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static nl.svendubbeld.fontys.test.matcher.RegexMatcher.matchesPattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HomePageStepdefs {

    private Response response;

    @Inject
    private World world;

    @Inject
    private TransactionalTests transactionalTests;

    @When("^I search for \"([^\"]*)\"$")
    public void search(String query) {
        response = given()
                .param("query", query)
                .when()
                .get("/tweets/search");
    }

    @Then("^I get (\\d+) tweets containing \"([^\"]*)\"$")
    public void contains(int count, String query) {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("", hasSize(count))
                .and()
                .body("content", everyItem(containsString(query)));
    }

    @Then("^I get (\\d+) tweets from \"([^\"]*)\"$")
    public void fromOwner(int count, String query) {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("", hasSize(count))
                .and()
                .body("owner.profile.username", everyItem(is(query)));
    }

    @Then("^I get (\\d+) tweets$")
    public void totalTweets(int count) {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("", hasSize(count));
    }

    @When("^I place a tweet containing \"([^\"]*)\"$")
    public void placeTweet(String content) {
        TweetDTO tweet = new TweetDTO();

        tweet.setContent(content);

        response = given()
                .auth()
                .oauth2(world.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(tweet)
                .when()
                .post("/tweets");
    }

    @Then("^The server responds with (\\d+) and redirects me to \"([^\"]*)\"$")
    public void serverRedirect(int code, String path) {
        response.then()
                .statusCode(code)
                .header("Location", matchesPattern(Pattern.compile("^" + world.getBaseUri() + path + "$")));
    }

    @Then("^A tweet containing \"([^\"]*)\" from \"([^\"]*)\" should exist$")
    public void tweetContains(String content, String username) {
        transactionalTests.tweetContains(content, username);
    }

    @When("^I load my timeline$")
    public void loadTimeline() {
        response = given()
                .auth()
                .oauth2(world.getToken())
                .when()
                .get("/me/timeline");
    }

    @And("^A tweet mentioning \"([^\"]*)\" should exist$")
    public void tweetMentions(String username) {
        transactionalTests.tweetMentions(username);
    }

    @When("^I load my mentions$")
    public void loadMentions() {
        response = given()
                .auth()
                .oauth2(world.getToken())
                .when()
                .get("/me/mentions");
    }

    @And("^A tweet with hashtag \"([^\"]*)\" should exist$")
    public void tweetWithHashtag(String hashtag) {
        transactionalTests.tweetWithHashtag(hashtag);
    }

    @When("^I load popular trends$")
    public void loadTrends() {
        response = given()
                .auth()
                .oauth2(world.getToken())
                .when()
                .get("/me/trends");
    }

    @Then("^I get (\\d+) times hashtag \"([^\"]*)\"$")
    public void timesHashtag(int expected, String hashtag) {
        // Use Gson parser because JsonPath shits itself when it encounters an '#' symbol
        JsonObject jsonObject = new JsonParser().parse(response.then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .asString())
                .getAsJsonObject();

        int amount = jsonObject
                .get(hashtag)
                .getAsInt();

        assertThat(amount, is(expected));
    }
}
