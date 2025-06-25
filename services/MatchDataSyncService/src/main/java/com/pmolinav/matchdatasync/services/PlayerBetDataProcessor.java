package com.pmolinav.matchdatasync.services;

import com.pmolinav.leagueslib.dto.MatchDayDTO;
import com.pmolinav.matchdatasync.dto.ExternalMatchScoreDTO;
import com.pmolinav.matchdatasync.dto.ExternalScoreDTO;
import com.pmolinav.predictionslib.dto.MatchStatus;
import com.pmolinav.predictionslib.dto.PlayerBetStatus;
import com.pmolinav.predictionslib.model.Match;
import com.pmolinav.predictionslib.model.Odds;
import com.pmolinav.predictionslib.model.PlayerBet;
import com.pmolinav.predictionslib.model.PlayerBetSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlayerBetDataProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBetDataProcessor.class);

    private final MatchService matchService;
    private final EventService eventService;
    private final OddsService oddsService;
    private final PlayerBetService playerBetService;
    private final PlayerBetSelectionService playerBetSelectionService;

    public PlayerBetDataProcessor(MatchService matchService,
                                  EventService eventService,
                                  OddsService oddsService,
                                  PlayerBetService playerBetService,
                                  PlayerBetSelectionService playerBetSelectionService) {
        this.matchService = matchService;
        this.eventService = eventService;
        this.oddsService = oddsService;
        this.playerBetService = playerBetService;
        this.playerBetSelectionService = playerBetSelectionService;
    }

    private static final int PAGE_SIZE = 500;

    public boolean processResults(MatchDayDTO matchDayDTO, List<ExternalMatchScoreDTO> results) {
        boolean allProcessed = true;

        // Fetch matches from internal DB for this match day.
        List<Match> activeMatches = matchService.findByCategoryIdAndSeasonAndMatchDayNumber(
                matchDayDTO.getCategoryId(),
                matchDayDTO.getSeason(),
                matchDayDTO.getMatchDayNumber()
        );

        if (activeMatches.isEmpty()) {
            logger.warn("No matches found for MatchDay: {}", matchDayDTO);
            return false;
        }

        // Only ACTIVE matches can be resolved.
        activeMatches = activeMatches.stream()
                .filter(match -> match.getStatus().equals(MatchStatus.ACTIVE))
                .toList();

        // Map results from external by externalId.
        Map<String, ExternalMatchScoreDTO> resultMap = results.stream()
                .collect(Collectors.toMap(ExternalMatchScoreDTO::getId, Function.identity()));

        // Process each active match.
        for (Match match : activeMatches) {
            int page = 0;
            boolean hasMore = true;

            // Page of player bets per match.
            while (hasMore) {
                Page<PlayerBet> pendingBetsPage = playerBetService.findPendingBetsByMatchId(
                        match.getMatchId(),
                        page,
                        PAGE_SIZE
                );

                List<PlayerBet> updatedBets = new ArrayList<>();
                List<PlayerBetSelection> updatedSelections = new ArrayList<>();

                for (PlayerBet bet : pendingBetsPage.getContent()) {
                    try {
                        ExternalMatchScoreDTO externalResult = resultMap.get(match.getExternalId());
                        if (externalResult == null || !externalResult.isCompleted()) {
                            logger.warn("No external result found for match ID: {}", match.getExternalId());
                            allProcessed = false;
                            continue;
                        }

                        boolean allSelectionsCorrect = true;

                        for (PlayerBetSelection selection : bet.getSelections()) {
                            boolean correct = isPredictionCorrect(selection, externalResult);

                            selection.setStatus(correct ? PlayerBetStatus.WON : PlayerBetStatus.LOST);
                            updatedSelections.add(selection);

                            if (!correct) {
                                allSelectionsCorrect = false;
                            }
                        }

                        bet.setStatus(allSelectionsCorrect ? PlayerBetStatus.WON : PlayerBetStatus.LOST);
                        updatedBets.add(bet);

                    } catch (Exception e) {
                        logger.error("Unexpected error occurred while processing PlayerBet with ID: {}", bet.getBetId(), e);
                    }
                }

                // Save processed bets and selections in batch.
                if (!updatedBets.isEmpty()) {
                    playerBetService.saveAll(updatedBets);
                }
                if (!updatedSelections.isEmpty()) {
                    playerBetSelectionService.saveAll(updatedSelections);
                }

                hasMore = pendingBetsPage.hasNext();
                page++;
            }

            match.setStatus(MatchStatus.RESOLVED);
            matchService.updateMatch(match);
        }

        // TODO: POINTS BY STAKE!!
        return allProcessed;
    }

    public boolean isPredictionCorrect(PlayerBetSelection selection, ExternalMatchScoreDTO result) {
        Odds odds = oddsService.findOddsById(selection.getOddsId()); // Non-cacheable

        String eventKey = selection.getEventType(); // e.g. "h2h", "totals", "spreads"
        String label = odds.getLabel();
        BigDecimal point = odds.getPoint();

        // Evaluate only what's needed for the event type
        return switch (eventKey) {
            case "h2h" -> evaluateH2H(result.getScores(), label);
            case "totals" -> evaluateTotals(result.getScores(), odds);
            case "spreads" -> evaluateSpreads(result.getScores(), label, point);
            default -> {
                logger.error("Unsupported event key: {}. False is returned.", eventKey);
                yield false;
            }
        };
    }

    private boolean evaluateH2H(List<ExternalScoreDTO> scores, String label) {
        if (scores.size() != 2) {
            throw new IllegalArgumentException("Invalid number of scores in external match result");
        }

        ExternalScoreDTO team1 = scores.get(0);
        ExternalScoreDTO team2 = scores.get(1);
        int score1 = Integer.parseInt(team1.getScore());
        int score2 = Integer.parseInt(team2.getScore());

        if ("Draw".equalsIgnoreCase(label)) {
            return score1 == score2;
        }

        return (label.equals(team1.getName()) && score1 > score2)
                || (label.equals(team2.getName()) && score2 > score1);
    }

    private boolean evaluateTotals(List<ExternalScoreDTO> scores, Odds odds) {
        if (scores.size() != 2) {
            throw new IllegalArgumentException("Invalid number of scores in external match result");
        }

        int total = Integer.parseInt(scores.get(0).getScore()) + Integer.parseInt(scores.get(1).getScore());
        BigDecimal totalDecimal = BigDecimal.valueOf(total);
        String label = odds.getLabel().toUpperCase();

        if (label.contains("OVER")) {
            return totalDecimal.compareTo(odds.getPoint()) > 0;
        } else if (label.contains("UNDER")) {
            return totalDecimal.compareTo(odds.getPoint()) < 0;
        }

        throw new IllegalArgumentException("Invalid totals label: " + odds.getLabel());
    }

    private boolean evaluateSpreads(List<ExternalScoreDTO> scores, String label, BigDecimal point) {
        if (scores.size() != 2) {
            throw new IllegalArgumentException("Invalid number of scores in external match result");
        }

        ExternalScoreDTO team1 = scores.get(0);
        ExternalScoreDTO team2 = scores.get(1);
        boolean isFirstTeam = team1.getName().equals(label);
        boolean isSecondTeam = team2.getName().equals(label);

        if (!isFirstTeam && !isSecondTeam) {
            throw new IllegalArgumentException("Label " + label + " does not match any team");
        }

        BigDecimal teamScore = BigDecimal.valueOf(isFirstTeam
                ? Integer.parseInt(team1.getScore())
                : Integer.parseInt(team2.getScore())).add(point);

        BigDecimal opponentScore = BigDecimal.valueOf(isFirstTeam
                ? Integer.parseInt(team2.getScore())
                : Integer.parseInt(team1.getScore()));

        return teamScore.compareTo(opponentScore) > 0;
    }

}