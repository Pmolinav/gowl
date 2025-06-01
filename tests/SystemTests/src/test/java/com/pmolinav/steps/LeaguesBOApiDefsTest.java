package com.pmolinav.steps;

import io.cucumber.java.en.When;

public class LeaguesBOApiDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8005";

    @When("^try to get LeaguesBOApi health")
    public void tryToGetHealth() {
        executeGet(localURL + "/health");
    }
}