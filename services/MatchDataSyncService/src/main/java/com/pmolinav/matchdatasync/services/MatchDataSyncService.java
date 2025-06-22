package com.pmolinav.matchdatasync.services;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.matchdatasync.clients.ExternalMatchClient;
import com.pmolinav.matchdatasync.clients.MatchDaysClient;
import com.pmolinav.matchdatasync.dto.ExternalMatchDTO;
import com.pmolinav.predictionslib.model.ExternalCategoryMapping;
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

    @Value("${sync.matchday.offset-ms}")
    private long offsetMs;
    @Value("${external.api.key}")
    private String apiKey;

    public MatchDataSyncService(MatchDaysClient matchDaysClient,
                                ExternalMatchClient externalMatchClient,
                                ExternalCategoryMappingService externalCategoryMappingService,
                                MatchDataProcessor matchDataProcessor) {
        this.matchDaysClient = matchDaysClient;
        this.externalMatchClient = externalMatchClient;
        this.externalCategoryMappingService = externalCategoryMappingService;
        this.matchDataProcessor = matchDataProcessor;
    }

    @Scheduled(fixedRateString = "${sync.match.rate-ms}")
    public void scheduleMatchDaySync() {
        long now = System.currentTimeMillis();
        long dateTo = now + offsetMs;

        try {
            List<MatchDayDTO> upcomingMatchDays = matchDaysClient.findAllMatchDays(now, dateTo, false);

            if (upcomingMatchDays.isEmpty()) {
                logger.debug("No unsynced matchDays were found to be synced by dateFrom {} and dateTo {}", now, dateTo);
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
}

