package com.pmolinav.matchdatasync.services;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.matchdatasync.dto.ExternalBookmakerDTO;
import com.pmolinav.matchdatasync.dto.ExternalMarketDTO;
import com.pmolinav.matchdatasync.dto.ExternalMatchDTO;
import com.pmolinav.matchdatasync.exceptions.InternalServerErrorException;
import com.pmolinav.matchdatasync.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.MatchDTO;
import com.pmolinav.predictionslib.dto.MatchStatus;
import com.pmolinav.predictionslib.dto.OddsDTO;
import com.pmolinav.predictionslib.model.Event;
import com.pmolinav.predictionslib.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    public boolean processMatches(MatchDayDTO matchDayDTO, List<ExternalMatchDTO> externalMatches) {
        final long TIME_MARGIN_MS = 18000000L; // 5 hours, for example.
        Set<String> storedExternalMatchIds = new HashSet<>();
        List<Match> scheduledMatches = new ArrayList<>();

        List<Match> existingMatches = matchService.findByCategoryIdAndSeasonAndMatchDayNumber(
                matchDayDTO.getCategoryId(),
                matchDayDTO.getSeason(),
                matchDayDTO.getMatchDayNumber()
        );

        if (!CollectionUtils.isEmpty(existingMatches)) {
            // Get already stored external Ids.
            storedExternalMatchIds = existingMatches.stream()
                    .map(Match::getExternalId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            // Filter matches by SCHEDULED to be updated.
            scheduledMatches = existingMatches.stream()
                    .filter(m -> m.getStatus().equals(MatchStatus.SCHEDULED))
                    .toList();
        }

        Set<Match> updatedMatches = new HashSet<>();

        for (ExternalMatchDTO externalMatch : externalMatches) {
            try {
                logger.debug("Processing external match with ID {}", externalMatch.getId());

                // If this match had been already processed previously, just skip it.
                if (storedExternalMatchIds.contains(externalMatch.getId())) {
                    logger.debug("Match with external ID {} is already stored. Skipping.", externalMatch.getId());
                    continue;
                }

                long externalStartTime = externalMatch.getCommence_time().toInstant().toEpochMilli();

                // Try to match external matches with stored matches. They will normally match.
                Optional<Match> matchingScheduled = scheduledMatches.stream()
                        .filter(m -> m.getHomeTeam().equalsIgnoreCase(externalMatch.getHome_team())
                                && m.getAwayTeam().equalsIgnoreCase(externalMatch.getAway_team())
                                && Math.abs(m.getStartTime() - externalStartTime) <= TIME_MARGIN_MS)
                        .findFirst();

                Match match;
                if (matchingScheduled.isPresent()) {
                    Match existing = matchingScheduled.get();
                    updatedMatches.add(existing);

                    existing.setStartTime(externalStartTime);
                    existing.setStatus(MatchStatus.ACTIVE);
                    existing.setExternalId(externalMatch.getId());

                    match = matchService.updateMatch(existing);
                } else {
                    // If not present yet (a strange situation), the new match is stored.
                    MatchDTO matchDTO = new MatchDTO(
                            matchDayDTO.getCategoryId(),
                            matchDayDTO.getSeason(),
                            matchDayDTO.getMatchDayNumber(),
                            externalMatch.getHome_team(),
                            externalMatch.getAway_team(),
                            externalStartTime,
                            MatchStatus.ACTIVE,
                            externalMatch.getId()
                    );
                    match = matchService.createMatch(matchDTO);
                }

                // Choose the provider with more markets.
                ExternalBookmakerDTO bestBookmaker = externalMatch.getBookmakers().stream()
                        .max(Comparator.comparingInt(b -> b.getMarkets().size()))
                        .orElseThrow(() -> new IllegalStateException("No bookmakers found for match " + externalMatch.getId()));

                logger.info("Bookmaker {} is the best one due to it has {} markets. Complete bookmaker chosen: {}",
                        bestBookmaker.getTitle(), bestBookmaker.getMarkets().size(), bestBookmaker);

                // For each market, get Event and create Odds.
                for (ExternalMarketDTO market : bestBookmaker.getMarkets()) {
                    Event event;
                    try {
                        event = eventService.cachedFindEventByType(market.getKey());
                    } catch (NotFoundException nFe) {
                        event = new Event(
                                market.getKey(),
                                market.getLink(),
                                null,
                                null
                        );
                    }

                    Event finalEvent = event;
                    List<OddsDTO> oddsDTOList = market.getOutcomes().stream()
                            .map(outcome -> new OddsDTO(
                                    finalEvent.getEventType(),
                                    match.getMatchId(),
                                    outcome.getName(),
                                    outcome.getPrice(),
                                    outcome.getPoint(),
                                    bestBookmaker.getKey(),
                                    true
                            ))
                            .toList();

                    oddsService.createOddsList(oddsDTOList);
                }

            } catch (Exception e) {
                logger.error("Unexpected error occurred while processing external match {}", externalMatch.getId(), e);
                throw new InternalServerErrorException("Unexpected error occurred while processing match " + externalMatch.getId());
            }
        }

        // Return true if all expected matches are synchronized.
        return scheduledMatches.size() == updatedMatches.size();
    }

}

