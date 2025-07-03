package com.pmolinav.matchdatasync.services;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.matchdatasync.clients.ExternalMatchClient;
import com.pmolinav.matchdatasync.clients.MatchDaysClient;
import com.pmolinav.matchdatasync.dto.ExternalMatchDTO;
import com.pmolinav.matchdatasync.dto.ExternalMatchScoreDTO;
import com.pmolinav.predictionslib.model.ExternalCategoryMapping;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchDataSyncService {

    private static final Logger logger = LoggerFactory.getLogger(MatchDataSyncService.class);

    private final MatchDaysClient matchDaysClient;
    private final ExternalMatchClient externalMatchClient;
    private final ExternalCategoryMappingService externalCategoryMappingService;
    private final MatchDataProcessor matchDataProcessor;
    private final PlayerBetDataProcessor playerBetDataProcessor;

    @Value("${sync.matchday.start.offset-ms}")
    private long offsetStartMs;
    @Value("${sync.matchday.end.offset-ms}")
    private long offsetEndMs;
    @Value("${external.api.key}")
    private String apiKey;

    public MatchDataSyncService(MatchDaysClient matchDaysClient,
                                ExternalMatchClient externalMatchClient,
                                ExternalCategoryMappingService externalCategoryMappingService,
                                MatchDataProcessor matchDataProcessor,
                                PlayerBetDataProcessor playerBetDataProcessor) {
        this.matchDaysClient = matchDaysClient;
        this.externalMatchClient = externalMatchClient;
        this.externalCategoryMappingService = externalCategoryMappingService;
        this.matchDataProcessor = matchDataProcessor;
        this.playerBetDataProcessor = playerBetDataProcessor;
    }

    @Scheduled(fixedRateString = "${sync.matches.rate-ms}")
    public void scheduleMatchDaySync() {
        long now = System.currentTimeMillis();
        long dateTo = now + offsetStartMs;

        try {
            List<MatchDayDTO> upcomingMatchDays;
            try {
                upcomingMatchDays = matchDaysClient.findAllMatchDays(now, dateTo, false);
                if (upcomingMatchDays.isEmpty()) {
                    logger.info("No unsynced matchDays were found to be synced by dateFrom {} and dateTo {}", now, dateTo);
                    return;
                }
            } catch (FeignException.NotFound nf) {
                logger.info("No unsynced matchDays were found to be synced by dateFrom {} and dateTo {}", now, dateTo, nf);
                return;
            }

            for (MatchDayDTO matchDay : upcomingMatchDays) {
                try {
                    logger.debug("Synchronizing MatchDay: {}.", matchDay);

                    // Get external sportKey by categoryId (cached).
                    ExternalCategoryMapping sportKeyMapping = externalCategoryMappingService.cachedFindById(matchDay.getCategoryId());
                    // Get matches from external API.
                    List<ExternalMatchDTO> matches = externalMatchClient.fetchOdds(matchDay, sportKeyMapping.getExternalSportKey(), apiKey);
                    // Store data on our side.
                    boolean completed = matchDataProcessor.processMatches(matchDay, matches);
                    // Set match day as synced if all matches are completed.
                    if (completed) {
                        matchDay.setSynced(true);
                        matchDaysClient.updateMatchDay(matchDay);
                    } else {
                        logger.info("Some matches were not completed yet for MatchDay: {}.", matchDay);
                    }
                    logger.debug("MatchDay synchronized successfully");
                } catch (Exception e) {
                    logger.error("Unexpected error occurred synchronizing MatchDay: {}", matchDay, e);
                }
            }

        } catch (Exception e) {
            logger.error("Unexpected generic error occurred synchronizing match days.", e);
        }
    }

    @Scheduled(fixedRateString = "${sync.results.rate-ms}")
    public void schedulePlayerBetResultCheck() {
        try {
            long now = System.currentTimeMillis();
            long dateFrom = now - offsetEndMs;

            List<MatchDayDTO> completedMatchDays;
            try {
                completedMatchDays = matchDaysClient.findCompletedMatchDays(dateFrom, now, false);
                if (completedMatchDays.isEmpty()) {
                    logger.info("No completed match days pending result check were found by dateFrom {} and dateTo {}", dateFrom, now);
                    return;
                }
            } catch (FeignException.NotFound nf) {
                logger.info("No completed match days pending result check were found by dateFrom {} and dateTo {}", dateFrom, now, nf);
                return;
            }

            for (MatchDayDTO matchDay : completedMatchDays) {
                try {
                    logger.debug("Checking results for MatchDay: {}", matchDay);

                    ExternalCategoryMapping mapping = externalCategoryMappingService.cachedFindById(matchDay.getCategoryId());

                    List<ExternalMatchScoreDTO> results = externalMatchClient.fetchResults(
                            mapping.getExternalSportKey(),
                            apiKey,
                            3 // This can be configurable too
                    );

                    boolean allChecked = playerBetDataProcessor.processResults(matchDay, results);

                    if (allChecked) {
                        matchDay.setResultsChecked(true);
                        matchDaysClient.updateMatchDay(matchDay);
                    } else {
                        logger.info("Some player bets are still pending result resolution for MatchDay: {}", matchDay);
                    }

                } catch (Exception e) {
                    logger.error("Error while checking results for MatchDay: {}", matchDay, e);
                }
            }
        } catch (Exception e) {
            logger.error("Generic error occurred while checking player bet results.", e);
        }
    }

}

