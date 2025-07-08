package com.pmolinav.matchdatasync.clients;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.matchdatasync.dto.ExternalMatchDTO;
import com.pmolinav.matchdatasync.dto.ExternalMatchScoreDTO;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ExternalMatchClient {

    private static final Logger logger = LoggerFactory.getLogger(ExternalMatchClient.class);

    private final WebClient webClient;

    public ExternalMatchClient(@Value("${external.api.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public List<ExternalMatchDTO> fetchOdds(MatchDayDTO matchDay, String sportKey, String apiKey) {
        // Converting timestamps to ISO-8601 UTC format.
        String commenceTimeFrom = Instant.ofEpochMilli(matchDay.getStartDate())
                .atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        String commenceTimeTo = Instant.ofEpochMilli(matchDay.getEndDate())
                .atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/sports/" + sportKey + "/odds")
                            .queryParam("apiKey", apiKey)
                            .queryParam("regions", "eu")
                            .queryParam("markets", "h2h,spreads,totals")
                            .queryParam("dateFormat", "iso")
                            .queryParam("oddsFormat", "decimal")
                            .queryParam("commenceTimeFrom", commenceTimeFrom)
                            .queryParam("commenceTimeTo", commenceTimeTo)
                            .queryParam("includeLinks", "false")
                            .queryParam("includeSids", "false")
                            .queryParam("includeBetLimits", "false")
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                                logger.error("Unexpected external API Error was returned with status code {} and body: {}",
                                        clientResponse.statusCode(), errorBody);
                                return Mono.error(new RuntimeException("External API error: " + errorBody));
                            }))
                    .bodyToFlux(ExternalMatchDTO.class)
                    .collectList()
                    .block(); // BLOCK! Because our code is sequential.
        } catch (Exception e) {
            logger.error("Failed to search odds from external API by commenceTimeFrom {} and commenceTimeTo {}",
                    commenceTimeFrom, commenceTimeTo, e);
            throw new InternalServerErrorException("Unexpected error occurred while calling external API.");
        }
    }

    public List<ExternalMatchScoreDTO> fetchResults(String sportKey, String apiKey, int daysFrom) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/sports/" + sportKey + "/scores")
                            .queryParam("apiKey", apiKey)
                            .queryParam("daysFrom", daysFrom)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                                logger.error("External API Error on scores endpoint. Status: {}, Body: {}",
                                        clientResponse.statusCode(), errorBody);
                                return Mono.error(new RuntimeException("External API error: " + errorBody));
                            }))
                    .bodyToFlux(ExternalMatchScoreDTO.class)
                    .collectList()
                    .block(); // BLOCK: synchronous call
        } catch (Exception e) {
            logger.error("Failed to fetch match results from external API for sportKey {} and daysFrom {}",
                    sportKey, daysFrom, e);
            throw new InternalServerErrorException("Unexpected error occurred while fetching match results.");
        }
    }

}


