package com.pmolinav.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.pmolinav.HeaderSettingRequestCallback;
import com.pmolinav.ResponseResults;
import com.pmolinav.database.LeaguesDatabaseConnector;
import com.pmolinav.database.PredictionsDatabaseConnector;
import com.pmolinav.database.UsersDatabaseConnector;
import com.pmolinav.leagueslib.model.*;
import com.pmolinav.predictionslib.model.*;
import com.pmolinav.systemtests.Main;
import com.pmolinav.userslib.model.User;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CucumberContextConfiguration
@SpringBootTest(classes = Main.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class BaseSystemTest {

    @Autowired
    protected RestTemplate restTemplate;
    protected static UsersDatabaseConnector usersDbConnector;
    protected static LeaguesDatabaseConnector leaguesDbConnector;
    protected static PredictionsDatabaseConnector predictionsDbConnector;
    protected final static ObjectMapper objectMapper = new ObjectMapper();
    protected static ResponseResults authResponse = null;
    protected static ResponseResults latestResponse = null;
    protected static String authToken;
    // Users
    protected static User lastUser;
    // Leagues
    protected static League lastLeague;
    protected static LeaguePlayer lastLeaguePlayer;
    protected static LeaguePlayerPoints lastLeaguePlayerPoints;
    protected static LeagueCategory lastLeagueCategory;
    protected static MatchDay lastMatchDay;
    // Predictions
    protected static Match lastMatch;
    protected static Event lastEvent;
    protected static Odds lastOdds;
    protected static PlayerBet lastPlayerBet;
    protected static PlayerBetSelection lastPlayerBetSelection;

    protected static final String WIREMOCK_HOST = "localhost";
    protected static final int WIREMOCK_PORT = 8888;
    protected static WireMock wireMock;

    protected void executeGet(String url) {
        executeGet(url, null);
    }

    protected void executeGet(String url, Map<String, Object> queryParams) {
        executeGet(url, UUID.randomUUID().toString(), queryParams);
    }

    void executeGet(String url, String requestUid, Map<String, Object> queryParams) {
        final Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT, "application/json");
        headers.put(HttpHeaders.AUTHORIZATION, authToken);

        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(null, headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url + "?requestUid=" + requestUid + composeQueryParams(queryParams),
                HttpMethod.GET, requestCallback, response -> {
                    if (errorHandler.hasError) {
                        return errorHandler.getResult();
                    } else {
                        return new ResponseResults(response);
                    }
                });
    }

    protected void executePut(String url) {
        executePut(url, null, null);
    }

    protected void executePut(String url, String body) {
        executePut(url, UUID.randomUUID().toString(), null, body);
    }

    protected void executePut(String url, Map<String, Object> queryParams) {
        executePut(url, UUID.randomUUID().toString(), queryParams, null);
    }

    protected void executePut(String url, Map<String, Object> queryParams, String body) {
        executePut(url, UUID.randomUUID().toString(), queryParams, body);
    }

    void executePut(String url, String requestUid, Map<String, Object> queryParams, String body) {
        final Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT, "application/json");
        headers.put(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.put(HttpHeaders.AUTHORIZATION, authToken);

        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(body, headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url + "?requestUid=" + requestUid + composeQueryParams(queryParams),
                HttpMethod.PUT, requestCallback, response -> {
                    if (errorHandler.hasError) {
                        return errorHandler.getResult();
                    } else {
                        return new ResponseResults(response);
                    }
                });
    }

    protected void executePost(String url, String body) {
        executePost(url, body, UUID.randomUUID().toString());
    }

    protected void executePost(String url, String body, String requestUid) {
        final Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.put(HttpHeaders.ACCEPT, "application/json");
        headers.put(HttpHeaders.AUTHORIZATION, authToken);

        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(body, headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate
                .execute(url + "?requestUid=" + requestUid, HttpMethod.POST, requestCallback, response -> {
                    if (errorHandler.hasError) {
                        return (errorHandler.getResult());
                    } else {
                        return new ResponseResults(response);
                    }
                });
        if (url.contains("/login") && latestResponse != null) {
            authResponse = new ResponseResults(latestResponse.getResponse(),
                    latestResponse.getStatus(),
                    latestResponse.getBody(),
                    latestResponse.getHeaders());
        }
    }

    protected void executeDelete(String url) {
        executeDelete(url, null);
    }

    protected void executeDelete(String url, Map<String, Object> queryParams) {
        executeDelete(url, UUID.randomUUID().toString(), queryParams);
    }

    void executeDelete(String url, String requestUid, Map<String, Object> queryParams) {
        final Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT, "application/json");
        headers.put(HttpHeaders.AUTHORIZATION, authToken);

        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(null, headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url + "?requestUid=" + requestUid + composeQueryParams(queryParams),
                HttpMethod.DELETE, requestCallback, response -> {
                    if (errorHandler.hasError) {
                        return (errorHandler.getResult());
                    } else {
                        return new ResponseResults(response);
                    }
                });
    }

    private String composeQueryParams(Map<String, Object> queryParams) {
        if (ObjectUtils.isEmpty(queryParams)) {
            return "";
        } else {
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                queryString.append("&")
                        .append(entry.getKey())
                        .append("=")
                        .append(entry.getValue());
            }
            return queryString.toString();
        }
    }

    private static class ResponseResultErrorHandler implements ResponseErrorHandler {
        private ResponseResults result = null;
        private Boolean hasError = false;

        private ResponseResults getResult() {
            return result;
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            hasError = response.getStatusCode().isError();
            return hasError;
        }

        @Override
        public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
            // If unauthorized request, we will only need the response status code.
            if (response.getStatusCode().value() == 401) {
                result = new ResponseResults(response, 401, null, null);
            } else {
                result = new ResponseResults(response);
            }
        }
    }

}