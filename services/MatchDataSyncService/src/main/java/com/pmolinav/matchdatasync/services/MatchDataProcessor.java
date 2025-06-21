package com.pmolinav.matchdatasync.services;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.matchdatasync.dto.ExternalBookmakerDTO;
import com.pmolinav.matchdatasync.dto.ExternalMarketDTO;
import com.pmolinav.matchdatasync.dto.ExternalMatchDTO;
import com.pmolinav.matchdatasync.dto.ExternalOutcomeDTO;
import com.pmolinav.matchdatasync.exceptions.InternalServerErrorException;
import com.pmolinav.predictionslib.dto.EventDTO;
import com.pmolinav.predictionslib.dto.MatchDTO;
import com.pmolinav.predictionslib.dto.OddsDTO;
import com.pmolinav.predictionslib.model.Event;
import com.pmolinav.predictionslib.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MatchDataProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MatchDataProcessor.class);

    private final MatchService matchService;
    private final EventService eventService;
    private final OddsService oddsService;

    public MatchDataProcessor(MatchService matchService,
                              EventService eventService,
                              OddsService oddsService) {
        this.matchService = matchService;
        this.eventService = eventService;
        this.oddsService = oddsService;
    }

    public void processMatches(MatchDayDTO matchDayDTO, List<ExternalMatchDTO> externalMatches) {
        for (ExternalMatchDTO externalMatch : externalMatches) {
            try {
                logger.debug("Processing external match with ID {}", externalMatch.getId());

                // Convert commence_time (OffsetDateTime) to millis.
                long startTime = externalMatch.getCommence_time().toInstant().toEpochMilli();

                // Create Match
                MatchDTO matchDTO = new MatchDTO(
                        matchDayDTO.getCategoryId(),
                        matchDayDTO.getSeason(),
                        matchDayDTO.getMatchDayNumber(),
                        externalMatch.getHome_team(),
                        externalMatch.getAway_team(),
                        startTime,
                        "ACTIVE",
                        externalMatch.getId()
                );
                Match createdMatch = matchService.createMatch(matchDTO);

                // Choose the provider with more markets.
                ExternalBookmakerDTO bestBookmaker = externalMatch.getBookmakers().stream()
                        .max(Comparator.comparingInt(b -> b.getMarkets().size()))
                        .orElseThrow(() -> new IllegalStateException("No bookmakers found for match " + externalMatch.getId()));

                logger.info("Bookmaker {} is the best one due to it has {} markets. Complete bookmaker chosen: {}",
                        bestBookmaker.getTitle(), bestBookmaker.getMarkets().size(), bestBookmaker);

                // For each market, create Event and Odds.
                for (ExternalMarketDTO market : bestBookmaker.getMarkets()) {
                    EventDTO eventDTO = new EventDTO(
                            createdMatch.getMatchId(),
                            market.getKey(),
                            market.getLink()
                    );
                    Event createdEvent = eventService.createEvent(eventDTO);

                    List<OddsDTO> oddsDTOList = new ArrayList<>();
                    for (ExternalOutcomeDTO outcome : market.getOutcomes()) {
                        oddsDTOList.add(new OddsDTO(
                                createdEvent.getEventId(),
                                outcome.getName(),
                                outcome.getPrice(),
                                outcome.getPoint(),
                                true
                        ));
                    }
                    oddsService.createOddsList(oddsDTOList);
                }

            } catch (Exception e) {
                logger.error("Unexpected error occurred while processing external match {}",
                        externalMatch.getId(), e);
                throw new InternalServerErrorException("Unexpected error occurred while calling external API.");
            }
        }
    }
}

