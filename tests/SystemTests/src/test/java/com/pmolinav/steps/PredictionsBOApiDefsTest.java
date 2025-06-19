package com.pmolinav.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.predictionslib.dto.*;
import com.pmolinav.predictionslib.model.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PredictionsBOApiDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8008";

    @Given("the following matches have been stored previously$")
    public void storeMatchesInDatabase(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            List<Match> matches = rows.stream()
                    .map(row -> {
                        Match match = new Match();
                        match.setMatchId(Long.parseLong(row.get("match_id")));
                        match.setCategoryId(row.get("category_id"));
                        match.setSeason(Integer.parseInt(row.get("season")));
                        match.setMatchDayNumber(Integer.parseInt(row.get("match_day_number")));
                        match.setHomeTeam(row.get("home_team"));
                        match.setAwayTeam(row.get("away_team"));
                        match.setStartTime(Long.parseLong(row.get("start_time")));
                        match.setStatus(row.get("status"));
                        match.setCreationDate(row.get("creation_date") != null ?
                                Long.parseLong(row.get("creation_date")) : null);
                        match.setModificationDate(row.get("modification_date") != null ?
                                Long.parseLong(row.get("modification_date")) : null);
                        return match;
                    })
                    .collect(Collectors.toList());

            predictionsDbConnector.insertMatches(matches);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("the following events have been stored previously$")
    public void storeEventsInDatabase(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            List<Event> events = rows.stream()
                    .map(row -> {
                        Event event = new Event();
                        event.setEventId(Long.parseLong(row.get("event_id")));
                        event.setMatchId(Long.parseLong(row.get("match_id")));
                        event.setName(row.get("name"));
                        event.setDescription(row.get("description"));
                        event.setCreationDate(row.get("creation_date") != null ?
                                Long.parseLong(row.get("creation_date")) : null);
                        event.setModificationDate(row.get("modification_date") != null ?
                                Long.parseLong(row.get("modification_date")) : null);
                        return event;
                    })
                    .collect(Collectors.toList());

            predictionsDbConnector.insertEvents(events);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("the following odds have been stored previously$")
    public void storeOddsInDatabase(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            List<Odds> oddsList = rows.stream()
                    .map(row -> {
                        Odds odds = new Odds();
                        odds.setOddsId(Long.parseLong(row.get("odds_id")));
                        odds.setEventId(Long.parseLong(row.get("event_id")));
                        odds.setLabel(row.get("label"));
                        odds.setValue(new BigDecimal(row.get("value")));
                        odds.setActive(Boolean.parseBoolean(row.get("active")));
                        odds.setCreationDate(row.get("creation_date") != null ?
                                Long.parseLong(row.get("creation_date")) : null);
                        odds.setModificationDate(row.get("modification_date") != null ?
                                Long.parseLong(row.get("modification_date")) : null);
                        return odds;
                    })
                    .collect(Collectors.toList());

            predictionsDbConnector.insertOdds(oddsList);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("the following player bets have been stored previously$")
    public void storePlayerBetsInDatabase(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            List<PlayerBet> playerBets = rows.stream()
                    .map(row -> {
                        PlayerBet bet = new PlayerBet();
                        bet.setBetId(Long.parseLong(row.get("bet_id")));
                        bet.setUsername(row.get("username"));
                        bet.setMatchId(Long.parseLong(row.get("match_id")));
                        bet.setCreationDate(row.get("creation_date") != null ?
                                Long.parseLong(row.get("creation_date")) : null);
                        return bet;
                    })
                    .collect(Collectors.toList());

            predictionsDbConnector.insertPlayerBets(playerBets);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("the following player bet selections have been stored previously$")
    public void storePlayerBetSelectionsInDatabase(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            List<PlayerBetSelection> selections = rows.stream()
                    .map(row -> {
                        PlayerBetSelection selection = new PlayerBetSelection();
                        selection.setBetId(Long.parseLong(row.get("bet_id")));
                        selection.setOddsId(Long.parseLong(row.get("odds_id")));
                        selection.setStake(new BigDecimal(row.get("stake")));
                        selection.setCreationDate(row.get("creation_date") != null ?
                                Long.parseLong(row.get("creation_date")) : null);
                        return selection;
                    })
                    .collect(Collectors.toList());

            predictionsDbConnector.insertPlayerBetSelections(selections);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get PredictionsBOApi health$")
    public void tryToGetHealth() {
        executeGet(localURL + "/health");
    }

    @When("^try to create a new match with data$")
    public void tryToCreateANewMatch(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Map<String, String> row = rows.getFirst();
        MatchDTO matchDto = new MatchDTO();
        matchDto.setMatchId(Long.parseLong(row.get("match_id")));
        matchDto.setCategoryId(row.get("category_id"));
        matchDto.setSeason(Integer.parseInt(row.get("season")));
        matchDto.setMatchDayNumber(Integer.parseInt(row.get("match_day_number")));
        matchDto.setHomeTeam(row.get("home_team"));
        matchDto.setAwayTeam(row.get("away_team"));
        matchDto.setStartTime(Long.parseLong(row.get("start_time")));
        matchDto.setStatus(row.get("status"));

        try {
            executePost(localURL + "/matches", objectMapper.writeValueAsString(matchDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to update a match with data$")
    public void tryToUpdateAMatch(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Map<String, String> row = rows.getFirst();
        MatchDTO matchDto = new MatchDTO();
        matchDto.setMatchId(Long.parseLong(row.get("match_id")));
        matchDto.setCategoryId(row.get("category_id"));
        matchDto.setSeason(Integer.parseInt(row.get("season")));
        matchDto.setMatchDayNumber(Integer.parseInt(row.get("match_day_number")));
        matchDto.setHomeTeam(row.get("home_team"));
        matchDto.setAwayTeam(row.get("away_team"));
        matchDto.setStartTime(Long.parseLong(row.get("start_time")));
        matchDto.setStatus(row.get("status"));

        try {
            executePut(localURL + "/matches/" + lastMatch.getMatchId(),
                    objectMapper.writeValueAsString(matchDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get all matches$")
    public void tryToGetAllMatches() {
        executeGet(localURL + "/matches");
    }

    @When("^try to get a match by matchId$")
    public void tryToGetMatchByMatchId() {
        executeGet(localURL + "/matches/" + lastMatch.getMatchId());
    }

    @When("^try to get a match by categoryId, season and matchDayNumber$")
    public void tryToGetMatchByCategoryIdSeasonAndMatchDayNumber() {
        executeGet(localURL + "/matches/" + lastMatch.getMatchId()
                + "/categories/" + lastMatch.getCategoryId()
                + "/seasons/" + lastMatch.getSeason()
                + "/number/" + lastMatch.getMatchDayNumber());
    }

    @When("^try to delete a match by matchId$")
    public void tryToDeleteMatchByMatchId() {
        executeDelete(localURL + "/matches/" + lastMatch.getMatchId());
    }

    @When("^try to create a new event with data$")
    public void tryToCreateANewEvent(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Map<String, String> row = rows.getFirst();
        EventDTO eventDto = new EventDTO();
        eventDto.setMatchId(Long.parseLong(row.get("match_id")));
        eventDto.setName(row.get("name"));
        eventDto.setDescription(row.get("description"));

        try {
            executePost(localURL + "/events", objectMapper.writeValueAsString(eventDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to update an event with data$")
    public void tryToUpdateAnEvent(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Map<String, String> row = rows.getFirst();
        EventDTO eventDto = new EventDTO();
        eventDto.setMatchId(Long.parseLong(row.get("match_id")));
        eventDto.setName(row.get("name"));
        eventDto.setDescription(row.get("description"));

        try {
            executePut(localURL + "/events/" + lastEvent.getEventId(),
                    objectMapper.writeValueAsString(eventDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get all events$")
    public void tryToGetAllEvents() {
        executeGet(localURL + "/events");
    }

    @When("^try to get an event by eventId$")
    public void tryToGetEventByEventId() {
        executeGet(localURL + "/events/" + lastEvent.getEventId());
    }

    @When("^try to get events by matchId$")
    public void tryToGetEventByMatchId() {
        executeGet(localURL + "/events/match/" + lastEvent.getMatchId());
    }

    @When("^try to delete an event by eventId$")
    public void tryToDeleteEventByEventId() {
        executeDelete(localURL + "/events/" + lastEvent.getEventId());
    }

    @When("^try to create new odds with data$")
    public void tryToCreateNewOdds(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Map<String, String> row = rows.getFirst();
        OddsDTO oddsDto = new OddsDTO();
        oddsDto.setEventId(Long.parseLong(row.get("event_id")));
        oddsDto.setLabel(row.get("label"));
        oddsDto.setValue(new BigDecimal(row.get("value")));
        oddsDto.setActive(Boolean.parseBoolean(row.get("active")));

        try {
            executePost(localURL + "/odds", objectMapper.writeValueAsString(oddsDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to update odds with data$")
    public void tryToUpdateOdds(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Map<String, String> row = rows.getFirst();
        OddsDTO oddsDto = new OddsDTO();
        oddsDto.setEventId(Long.parseLong(row.get("event_id")));
        oddsDto.setLabel(row.get("label"));
        oddsDto.setValue(new BigDecimal(row.get("value")));
        oddsDto.setActive(Boolean.parseBoolean(row.get("active")));

        try {
            executePut(localURL + "/odds/" + lastOdds.getOddsId(),
                    objectMapper.writeValueAsString(oddsDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get all odds$")
    public void tryToGetAllOdds() {
        executeGet(localURL + "/odds");
    }

    @When("^try to get odds by oddsId$")
    public void tryToGetOddsByOddsId() {
        executeGet(localURL + "/odds/" + lastOdds.getOddsId());
    }

    @When("^try to get odds by eventId$")
    public void tryToGetOddsByEventId() {
        executeGet(localURL + "/odds/event/" + lastOdds.getEventId());
    }

    @When("^try to delete odds by oddsId$")
    public void tryToDeleteOddsByOddsId() {
        executeDelete(localURL + "/odds/" + lastOdds.getOddsId());
    }

    @When("^try to create new player bet with data$")
    public void tryToCreateNewPlayerBet(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> row = rows.getFirst();

        PlayerBetDTO playerBetDto = new PlayerBetDTO();
        playerBetDto.setMatchId(Long.parseLong(row.get("match_id")));
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
                        selection.setStake(new BigDecimal(fields[1].trim()));
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


    @When("^try to get all player bets$")
    public void tryToGetAllPlayerBets() {
        executeGet(localURL + "/player-bets");
    }

    @When("^try to get a player bet by playerBetId$")
    public void tryToGetPlayerBetById() {
        executeGet(localURL + "/player-bets/" + lastPlayerBet.getBetId());
    }

    @When("^try to get player bets by matchId$")
    public void tryToGetPlayerBetsByMatchId() {
        executeGet(localURL + "/player-bets/match/" + lastPlayerBet.getMatchId());
    }

    @When("^try to get player bets by username$")
    public void tryToGetPlayerBetsByUsername() {
        executeGet(localURL + "/player-bets/username/" + lastPlayerBet.getUsername());
    }

    @When("^try to delete a player bet by playerBetId$")
    public void tryToDeletePlayerBetById() {
        executeDelete(localURL + "/player-bets/" + lastPlayerBet.getBetId());
    }

    @When("^try to get all player bet selections$")
    public void tryToGetAllPlayerBetSelections() {
        executeGet(localURL + "/player-bet-selections");
    }

    @When("^try to create a new player bet selection with data$")
    public void tryToCreateANewPlayerBetSelection(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        Map<String, String> row = rows.getFirst();
        PlayerBetSelectionDTO selectionDto = new PlayerBetSelectionDTO();
        selectionDto.setOddsId(Long.parseLong(row.get("odds_id")));
        selectionDto.setStake(new BigDecimal(row.get("stake")));

        try {
            executePost(localURL + "/player-bet-selections", objectMapper.writeValueAsString(selectionDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get a player bet selection by selectionId$")
    public void tryToGetPlayerBetSelectionBySelectionId() {
        executeGet(localURL + "/player-bet-selections/" + lastPlayerBetSelection.getSelectionId());
    }

    @When("^try to delete a player bet selection by selectionId$")
    public void tryToDeletePlayerBetSelectionBySelectionId() {
        executeDelete(localURL + "/player-bet-selections/" + lastPlayerBetSelection.getSelectionId());
    }

    @Then("a match with categoryId (\\w+) has been stored successfully$")
    public void aMatchByCategoryIdHasBeenStored(String categoryId) {
        try {
            List<Match> matches = predictionsDbConnector.getMatchesByCategoryId(categoryId);
            lastMatch = matches.getFirst();
            assertNotNull(lastMatch);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("last stored match home team is (.*) and away team is (.*)$")
    public void aMatchWithHomeAndAwayTeamHasBeenStored(String homeTeam, String awayTeam) {
        try {
            lastMatch = predictionsDbConnector.getMatchById(lastMatch.getMatchId());
            assertNotNull(lastMatch);
            assertEquals(homeTeam, lastMatch.getHomeTeam());
            assertEquals(awayTeam, lastMatch.getAwayTeam());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("an event with name (\\w+) has been stored successfully$")
    public void anEventByNameHasBeenStored(String name) {
        try {
            List<Event> events = predictionsDbConnector.getEventsByName(name);
            lastEvent = events.getFirst();
            assertNotNull(lastEvent);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("odds with label (.*) have been stored successfully$")
    public void oddsByLabelHaveBeenStored(String label) {
        try {
            List<Odds> oddsList = predictionsDbConnector.getOddsByLabel(label);
            lastOdds = oddsList.getFirst();
            assertNotNull(lastOdds);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("a player bet for username (\\w+) has been stored successfully$")
    public void aPlayerBetByUsernameHasBeenStored(String username) {
        try {
            List<PlayerBet> playerBets = predictionsDbConnector.getPlayerBetsByUsername(username);
            lastPlayerBet = playerBets.getFirst();
            assertNotNull(lastPlayerBet);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("a player bet selection for last player bet has been stored successfully$")
    public void aPlayerBetSelectionHasBeenStored() {
        try {
            lastPlayerBetSelection = predictionsDbConnector.getPlayerBetsSelectionByPlayerBetId(lastPlayerBet.getBetId());
            assertNotNull(lastPlayerBetSelection);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("a list of matches with categoryId (\\w+) is returned in response$")
    public void aListOfMatchesWithCategoryIdIsReturned(String categoryId) throws JsonProcessingException {
        List<Match> obtainedMatches = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<Match>>() {
        });
        assertNotNull(obtainedMatches);
        assertFalse(obtainedMatches.isEmpty());
        for (Match match : obtainedMatches) {
            assertEquals(categoryId, match.getCategoryId());
        }
    }

    @Then("a match with categoryId (\\w+) returned in response$")
    public void matchWithCategoryIdHasBeenStoredSuccessfully(String categoryId) throws JsonProcessingException {
        Match obtainedMatch = objectMapper.readValue(latestResponse.getBody(), Match.class);
        assertNotNull(obtainedMatch);
        assertEquals(categoryId, obtainedMatch.getCategoryId());
    }

    @Then("a list of matches with categoryIds (.*) are returned in response$")
    public void aListOfMatchesWithCategoryIdsIsReturned(String categoryIds) throws JsonProcessingException {
        List<String> categoryIdList = List.of(categoryIds.split(","));
        List<Match> obtainedMatches = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<Match>>() {
        });
        assertNotNull(obtainedMatches);
        for (String categoryId : categoryIdList) {
            assertTrue(obtainedMatches.stream().anyMatch(match -> match.getCategoryId().equals(categoryId)));
        }
    }

    @Then("an event with name (.+) has been returned in response$")
    public void eventWithNameHasBeenStoredSuccessfully(String name) throws JsonProcessingException {
        Event obtainedEvent = objectMapper.readValue(latestResponse.getBody(), Event.class);
        assertNotNull(obtainedEvent);
        assertEquals(name, obtainedEvent.getName());
    }

    @Then("a list of events with name (\\w+) is returned in response$")
    public void aListOfEventsWithNameIsReturned(String name) throws JsonProcessingException {
        List<Event> obtainedEvents = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<Event>>() {
        });
        assertNotNull(obtainedEvents);
        assertFalse(obtainedEvents.isEmpty());
        for (Event event : obtainedEvents) {
            assertEquals(name, event.getName());
        }
    }

    @Then("a list of events with names (.*) are returned in response$")
    public void aListOfEventsWithNamesIsReturned(String names) throws JsonProcessingException {
        List<String> nameList = List.of(names.split(","));
        List<Event> obtainedEvents = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<Event>>() {
        });
        assertNotNull(obtainedEvents);
        for (String name : nameList) {
            assertTrue(obtainedEvents.stream().anyMatch(event -> event.getName().equals(name)));
        }
    }

    @Then("odds with label (.*) are returned in response$")
    public void oddsWithLabelAreReturned(String label) throws JsonProcessingException {
        Odds obtainedOdds = objectMapper.readValue(latestResponse.getBody(), Odds.class);
        assertNotNull(obtainedOdds);
        assertEquals(label, obtainedOdds.getLabel());
    }

    @Then("a list of odds with labels (.*) are returned in response$")
    public void aListOfOddsWithLabelsAreReturned(String labels) throws JsonProcessingException {
        List<String> labelList = List.of(labels.split(","));
        List<Odds> obtainedOddsList = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<Odds>>() {
        });
        assertNotNull(obtainedOddsList);
        for (String label : labelList) {
            assertTrue(obtainedOddsList.stream().anyMatch(odds -> odds.getLabel().equals(label)));
        }
    }

    @Then("a player bet with username (\\w+) is returned in response$")
    public void aPlayerBetWithUsernameIsReturned(String username) throws JsonProcessingException {
        PlayerBet obtainedPlayerBet = objectMapper.readValue(latestResponse.getBody(), PlayerBet.class);
        assertNotNull(obtainedPlayerBet);
        assertEquals(username, obtainedPlayerBet.getUsername());
    }

    @Then("a list of player bets with usernames (.*) is returned in response$")
    public void aListOfPlayerBetsWithUsernamesAreReturned(String usernames) throws JsonProcessingException {
        List<String> usernamesList = List.of(usernames.split(","));
        List<PlayerBetDTO> playerBets = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<PlayerBetDTO>>() {
        });
        assertNotNull(playerBets);
        for (String username : usernamesList) {
            assertTrue(playerBets.stream().anyMatch(playerBet -> playerBet.getUsername().equals(username)));
        }
    }

    @Then("a player bet selection for last player bet is returned in response$")
    public void aPlayerBetSelectionForLastPlayerBetIsReturned() throws JsonProcessingException {
        PlayerBetSelection obtainedSelection = objectMapper.readValue(latestResponse.getBody(), PlayerBetSelection.class);
        assertNotNull(obtainedSelection);
        assertEquals(lastPlayerBet.getBetId(), obtainedSelection.getBetId());
    }

    @Then("a list of player bet selections for last player bet is returned in response$")
    public void aListOfPlayerBetSelectionsForBetIdsAreReturned() throws JsonProcessingException {
        List<PlayerBetSelection> obtainedSelections = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<PlayerBetSelection>>() {
        });
        assertNotNull(obtainedSelections);
        assertTrue(obtainedSelections.stream().allMatch(
                selection -> selection.getBetId().equals(lastPlayerBet.getBetId())));
    }
}