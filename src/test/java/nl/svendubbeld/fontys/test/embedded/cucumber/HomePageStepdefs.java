package nl.svendubbeld.fontys.test.embedded.cucumber;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import okhttp3.HttpUrl;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.jboss.arquillian.test.api.ArquillianResource;

import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class HomePageStepdefs {

    private Response response;

    @ArquillianResource
    private URL url;

    @When("^I search for \"([^\"]*)\"$")
    public void search(String query) {
        RestAssured.baseURI = new HttpUrl.Builder()
                .scheme(url.getProtocol())
                .host(url.getHost())
                .port(url.getPort())
                .addPathSegments(StringUtils.strip(url.getPath(), "/"))
                .addPathSegment("api")
                .build().toString();

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
}
