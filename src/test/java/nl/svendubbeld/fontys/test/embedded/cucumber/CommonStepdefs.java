package nl.svendubbeld.fontys.test.embedded.cucumber;

import cucumber.api.java.en.Given;
import io.restassured.RestAssured;
import okhttp3.HttpUrl;
import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.test.api.ArquillianResource;

import javax.inject.Inject;
import java.net.URL;

public class CommonStepdefs {

    @ArquillianResource
    private URL url;

    @Inject
    private DataSetLoader loader;

    @Inject
    private World world;

    @Given("^The test data set \"([^\"]*)\" is loaded$")
    public void loadTestData(String dataSet) {
        loader.loadTestData(dataSet);
    }

    @Given("^I am logged in as \"([^\"]*)\"$")
    public void loggedIn(String username) {
        world.setUsername(username);
        world.setToken(username);
    }

    @Given("^The REST client is configured to use \"([^\"]*)\"$")
    public void configureRestClient(String path) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(url.getProtocol())
                .host(url.getHost())
                .port(url.getPort())
                .addPathSegments(StringUtils.strip(url.getPath(), "/"))
                .addPathSegment(StringUtils.strip(path, "/"))
                .build();
        RestAssured.baseURI = httpUrl.toString();

        world.setPath(httpUrl.encodedPath());
        world.setBaseUri(httpUrl.toString());
    }
}
