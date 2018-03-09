package nl.svendubbeld.fontys.cucumber;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import okhttp3.HttpUrl;
import org.apache.http.HttpStatus;
import org.hamcrest.core.StringContains;
import org.jboss.arquillian.test.api.ArquillianResource;

import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

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
                .addPathSegments(url.getPath())
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
                .body("results", hasSize(count))
                .and()
                .body("results.content", hasItems(StringContains.containsString(query)));
    }

    @Then("^I get (\\d+) tweets from \"([^\"]*)\"$")
    public void fromOwner(int count, String query) throws Throwable {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("results", hasSize(count))
                .and()
                .body("results.owner", hasItems(query));
    }
}
