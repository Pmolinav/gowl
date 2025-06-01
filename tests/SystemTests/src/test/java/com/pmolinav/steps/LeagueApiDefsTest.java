package com.pmolinav.steps;

import io.cucumber.java.en.When;

public class LeagueApiDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8006";

    @When("^try to get LeagueApi health")
    public void tryToGetHealth() {
        executeGet(localURL + "/health");
    }
}