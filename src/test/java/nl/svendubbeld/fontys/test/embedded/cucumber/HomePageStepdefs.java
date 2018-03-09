package nl.svendubbeld.fontys.test.embedded.cucumber;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import nl.svendubbeld.fontys.dao.TweetRepository;
import nl.svendubbeld.fontys.model.Tweet;
import org.apache.http.HttpStatus;

import javax.inject.Inject;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static nl.svendubbeld.fontys.test.matcher.PredicateMatcher.matches;
import static nl.svendubbeld.fontys.test.matcher.RegexMatcher.matchesPattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HomePageStepdefs {

    private Response response;

    @Inject
    private World world;

    @Inject
    private TweetRepository tweetRepository;

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

    @When("^I place a tweet containing \"([^\"]*)\"$")
    public void placeTweet(String content) {
        response = given()
                .header("X-API_KEY", world.getToken())
                .body("")
                .when()
                .post("/tweets");
    }

    @Then("^The server responds with (\\d+) and redirects me to \"([^\"]*)\"$")
    public void serverRedirect(int code, String path) {
        response.then()
                .statusCode(code)
                .header("Location", matchesPattern(Pattern.compile("^" + world.getPath() + "/" + path + "$")));
    }

    @Then("^A tweet containing \"([^\"]*)\" from \"([^\"]*)\" should exist$")
    public void tweetContains(String content, String username) {
        List<? super Tweet> byContent = tweetRepository.findByContent(content).collect(Collectors.toList());

        assertThat(byContent, hasItem(matches((Tweet tweet) -> tweet.getOwner().getCurrentProfile().get().getUsername().equals(username))));
    }
}
