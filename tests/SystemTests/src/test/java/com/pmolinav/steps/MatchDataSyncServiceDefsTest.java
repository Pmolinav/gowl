package com.pmolinav.steps;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MatchDataSyncServiceDefsTest extends BaseSystemTest {

    @Given("mock odds response from external API$")
    public void givenOddsResponseMockedFromExternalApi() throws Exception {
        String expectedResponse = readJsonFromResource("utils/matches_events_response.json");

        wireMock.register(WireMock.get(WireMock.urlPathMatching("/sports/soccer_spain_la_liga/odds.*"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(expectedResponse)
                        .withStatus(200)));
    }

    @Then("wait (\\d+) seconds$")
    public void waitSecondsForScheduledProcess(int seconds) throws Exception {
        Thread.sleep(seconds * 1000L);
    }

    protected String readJsonFromResource(String relativePath) {
        try {
            // From the current test directory: src/test/java (as default).
            Path basePath = Paths.get("src/test/java")
                    .resolve(Paths.get(this.getClass()
                            .getPackageName().replace(".", "/"))); // Folder where the test is.

            Path fullPath = basePath.resolve(relativePath);
            return Files.readString(fullPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read JSON from relative file: " + relativePath, e);
        }
    }
}