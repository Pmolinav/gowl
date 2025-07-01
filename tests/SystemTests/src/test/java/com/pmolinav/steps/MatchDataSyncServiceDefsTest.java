package com.pmolinav.steps;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.pmolinav.predictionslib.model.ExternalCategoryMapping;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

public class MatchDataSyncServiceDefsTest extends BaseSystemTest {


    @Given("the following categories mappings have been stored previously$")
    public void storeMappingsInDatabase(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            List<ExternalCategoryMapping> mapping = rows.stream()
                    .map(row -> new ExternalCategoryMapping(row.get("category_id"), row.get("external_sport_key")))
                    .toList();

            // Insert each requested mapping.
            predictionsDbConnector.insertMappings(mapping);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("mock odds response from external API for external category mapping (\\w+)$")
    public void givenOddsResponseMockedFromExternalApi(String externalCategory) {
        String expectedResponse = readJsonFromResource("utils/matches_events_response.json");

        wireMock.register(WireMock.get(WireMock.urlPathMatching("/sports/" + externalCategory + "/odds.*"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(expectedResponse)
                        .withStatus(200)));
    }

    @Then("wait for (\\d+) seconds$")
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