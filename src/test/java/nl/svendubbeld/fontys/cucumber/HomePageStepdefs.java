package nl.svendubbeld.fontys.cucumber;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class HomePageStepdefs {
    @When("^I search for \"([^\"]*)\"$")
    public void search(String query) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^I get (\\d+) tweets containing \"([^\"]*)\"$")
    public void contains(Integer count, String query) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
