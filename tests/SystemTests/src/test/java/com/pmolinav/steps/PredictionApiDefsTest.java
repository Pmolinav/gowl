package com.pmolinav.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

public class PredictionApiDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8009";

    @When("^try to get PredictionApi health")
    public void tryToGetHealth() {
        executeGet(localURL + "/health");
    }

    @When("^try to get a match by matchId with public endpoint$")
    public void tryToGetMatchByMatchIdPublicEndpoint() {
        executeGet(localURL + "/matches/" + lastMatch.getMatchId());
    }

    @When("^try to get a match by categoryId, season and matchDayNumber with public endpoint$")
    public void tryToGetMatchByCategoryIdSeasonAndMatchDayNumberPublicEndpoint() {
        executeGet(localURL + "/matches/categories/" + lastMatch.getCategoryId()
                + "/seasons/" + lastMatch.getSeason()
                + "/number/" + lastMatch.getMatchDayNumber());
    }

    @When("^try to get an event by eventType with public endpoint$")
    public void tryToGetEventByEventTypePublicEndpoint() {
        executeGet(localURL + "/events/" + lastEvent.getEventType());
    }

    @When("^try to get odds by oddsId with public endpoint$")
    public void tryToGetOddsByOddsIdPublicEndpoint() {
        executeGet(localURL + "/odds/" + lastOdds.getOddsId());
    }

    @When("^try to get odds by eventType with public endpoint")
    public void tryToGetOddsByEventTypePublicEndpoint() {
        executeGet(localURL + "/odds/events/" + lastOdds.getEventType());
    }

    @When("^try to get odds by matchId with public endpoint$")
    public void tryToGetOddsByMatchIdPublicEndpoint() {
        executeGet(localURL + "/odds/match/" + lastOdds.getMatchId());
    }

    @When("^try to create new player bet with data with public endpoint$")
    public void tryToCreateNewPlayerBetPublicEndpoint(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = rows.getFirst();

        PlayerBetDTO playerBetDto = new PlayerBetDTO();
        playerBetDto.setMatchId(Long.parseLong(row.get("match_id")));
        playerBetDto.setLeagueId(row.get("league_id") == null ? lastLeague.getLeagueId() : Long.parseLong(row.get("league_id")));
        playerBetDto.setUsername(row.get("username"));

        List<PlayerBetSelectionDTO> selections = new ArrayList<>();

        if (row.containsKey("selections")) {
            String selectionsRaw = row.get("selections").trim();
            if (!selectionsRaw.isEmpty()) {
                String[] selectionParts = selectionsRaw.split(";");
                for (String selectionStr : selectionParts) {
                    String[] fields = selectionStr.trim().split(",");
                    if (fields.length == 3) {
                        PlayerBetSelectionDTO selection = new PlayerBetSelectionDTO();
                        selection.setOddsId(Long.parseLong(fields[0].trim()));
                        selection.setEventType(fields[1].trim());
                        selection.setStake(new BigDecimal(fields[2].trim()));
                        selections.add(selection);
                    } else {
                        fail("Invalid selection format: " + selectionStr);
                    }
                }
            }
        }

        playerBetDto.setSelections(selections);

        try {
            executePost(localURL + "/player-bets", objectMapper.writeValueAsString(playerBetDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to create new player bet with last data with public endpoint$")
    public void tryToCreateNewPlayerBetForLastLeagueAndOddsPublicEndpoint(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = rows.getFirst();

        PlayerBetDTO playerBetDto = new PlayerBetDTO();
        playerBetDto.setMatchId(Long.parseLong(row.get("match_id")));
        playerBetDto.setLeagueId(row.get("league_id") == null ? lastLeague.getLeagueId() : Long.parseLong(row.get("league_id")));
        playerBetDto.setUsername(row.get("username"));

        List<PlayerBetSelectionDTO> selections = new ArrayList<>();

        if (row.containsKey("selections")) {
            String selectionsRaw = row.get("selections").trim();
            if (!selectionsRaw.isEmpty()) {
                String[] selectionParts = selectionsRaw.split(";");
                for (String selectionStr : selectionParts) {
                    String[] fields = selectionStr.trim().split(",");
                    if (fields.length == 3) {
                        PlayerBetSelectionDTO selection = new PlayerBetSelectionDTO();
                        selection.setOddsId(Long.parseLong(fields[0].trim()));
                        selection.setEventType(fields[1].trim());
                        selection.setStake(new BigDecimal(fields[2].trim()));
                        selections.add(selection);
                    } else {
                        fail("Invalid selection format: " + selectionStr);
                    }
                }
            }
        }

        playerBetDto.setSelections(selections);

        try {
            executePost(localURL + "/player-bets", objectMapper.writeValueAsString(playerBetDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get a player bet by playerBetId with public endpoint$")
    public void tryToGetPlayerBetByIdPublicEndpoint() {
        executeGet(localURL + "/player-bets/" + lastPlayerBet.getBetId());
    }

    @When("^try to get player bets by matchId with public endpoint$")
    public void tryToGetPlayerBetsByMatchIdPublicEndpoint() {
        executeGet(localURL + "/player-bets/match/" + lastPlayerBet.getMatchId());
    }

    @When("^try to get player bets by username with public endpoint$")
    public void tryToGetPlayerBetsByUsernamePublicEndpoint() {
        executeGet(localURL + "/player-bets/username/" + lastPlayerBet.getUsername());
    }

    @When("^try to delete a player bet by playerBetId with public endpoint$")
    public void tryToDeletePlayerBetByIdPublicEndpoint() {
        executeDelete(localURL + "/player-bets/" + lastPlayerBet.getBetId());
    }

}