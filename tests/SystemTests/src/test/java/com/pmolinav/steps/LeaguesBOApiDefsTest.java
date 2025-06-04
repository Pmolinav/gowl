package com.pmolinav.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pmolinav.database.LeaguesDatabaseConnector;
import com.pmolinav.leagueslib.dto.*;
import com.pmolinav.leagueslib.model.LeagueCategory;
import com.pmolinav.leagueslib.model.LeagueStatus;
import com.pmolinav.leagueslib.model.MatchDay;
import com.pmolinav.leagueslib.model.PlayerStatus;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class LeaguesBOApiDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8005";

    @Given("the following categories have been stored previously$")
    public void storeCategoriesInDatabase(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            List<LeagueCategory> categories = rows.stream()
                    .map(row -> new LeagueCategory(
                            row.get("category_id"),
                            row.get("name"),
                            row.get("description"),
                            row.get("sport"),
                            row.get("country"),
                            row.get("icon_url") != null ? row.get("icon_url") : null,
                            Boolean.parseBoolean(row.get("is_active")),
                            row.get("creation_date") != null ?
                                    Long.parseLong(row.get("creation_date")) : new Date().getTime(),
                            row.get("modification_date") != null ?
                                    Long.parseLong(row.get("modification_date")) : new Date().getTime()))
                    .collect(Collectors.toList());

            // Insert each requested league category.
            leaguesDbConnector.insertCategories(categories);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Given("the following match days have been stored previously$")
    public void storeMatchDaysInDatabase(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        try {
            List<MatchDay> matchDays = rows.stream()
                    .map(row -> {
                        MatchDay matchDay = new MatchDay();
                        matchDay.setCategoryId(row.get("category_id"));
                        matchDay.setSeason(Integer.parseInt(row.get("season")));
                        matchDay.setMatchDayNumber(Integer.parseInt(row.get("match_day_number")));
                        matchDay.setStartDate(Long.parseLong(row.get("start_date")));
                        matchDay.setEndDate(Long.parseLong(row.get("end_date")));
                        return matchDay;
                    })
                    .collect(Collectors.toList());

            // Insert each requested match day.
            leaguesDbConnector.insertMatchDays(matchDays);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get LeaguesBOApi health")
    public void tryToGetHealth() {
        executeGet(localURL + "/health");
    }

    @When("^try to create a new league category with data$")
    public void tryToCreateANewLeagueCategory(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // Just one league per test.
        Map<String, String> row = rows.getFirst();
        LeagueCategoryDTO leagueCategoryDto = new LeagueCategoryDTO();
        leagueCategoryDto.setCategoryId(row.get("category_id"));
        leagueCategoryDto.setName(row.get("name"));
        leagueCategoryDto.setDescription(row.get("description"));
        leagueCategoryDto.setSport(row.get("sport"));
        leagueCategoryDto.setCountry(row.get("country"));
        leagueCategoryDto.setIconUrl(row.get("icon_url"));
        leagueCategoryDto.setActive(Boolean.parseBoolean(row.get("is_active")));

        try {
            executePost(localURL + "/categories", objectMapper.writeValueAsString(leagueCategoryDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }


    @When("^try to get all league categories$")
    public void tryToGetAllLeagueCategories() {
        executeGet(localURL + "/categories");
    }

    @When("^try to get a league category by categoryId")
    public void tryToGetALeagueCategoryByCategoryId() {
        executeGet(localURL + "/categories/" + lastLeagueCategory.getCategoryId());
    }

    @When("^try to delete a league category by categoryId")
    public void tryToDeleteALeagueCategoryByCategoryId() {
        executeDelete(localURL + "/categories/" + lastLeagueCategory.getCategoryId());
    }

    @When("^try to create a new match day with data$")
    public void tryToCreateANewMatchDay(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // Just one league per test.
        Map<String, String> row = rows.getFirst();
        MatchDayDTO matchDayDTO = new MatchDayDTO();
        matchDayDTO.setCategoryId(row.get("category_id"));
        matchDayDTO.setSeason(Integer.parseInt(row.get("season")));
        matchDayDTO.setMatchDayNumber(Integer.parseInt(row.get("match_day_number")));
        matchDayDTO.setStartDate(Long.parseLong(row.get("start_date")));
        matchDayDTO.setEndDate(Long.parseLong(row.get("end_date")));

        try {
            executePost(localURL + "/match-days", objectMapper.writeValueAsString(matchDayDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to create several new match days with data$")
    public void tryToCreateSeveralMatchDays(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<MatchDayDTO> matchDayDTOList = new ArrayList<>();

        for (Map<String, String> row : rows) {
            matchDayDTOList.add(new MatchDayDTO(row.get("category_id"),
                    Integer.parseInt(row.get("season")),
                    Integer.parseInt(row.get("match_day_number")),
                    Long.parseLong(row.get("start_date")),
                    Long.parseLong(row.get("end_date"))
            ));
        }

        try {
            executePost(localURL + "/match-days/bulk", objectMapper.writeValueAsString(matchDayDTOList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get all match days$")
    public void tryToGetAllMatchDays() {
        executeGet(localURL + "/match-days");
    }

    @When("^try to get match days by categoryId")
    public void tryToGetMatchDaysByCategoryId() {
        executeGet(localURL + "/match-days/categories/" + lastMatchDay.getCategoryId());
    }

    @When("^try to get match days by categoryId and season")
    public void tryToGetMatchDaysByCategoryIdAndSeason() {
        executeGet(localURL + "/match-days/categories/" + lastMatchDay.getCategoryId()
                + "/seasons/" + lastMatchDay.getSeason());
    }

    @When("^try to delete match days by categoryId")
    public void tryToDeleteMatchDaysByCategoryId() {
        executeDelete(localURL + "/match-days/categories/" + lastMatchDay.getCategoryId());
    }

    @When("^try to delete match days by categoryId and season")
    public void tryToDeleteMatchDaysByCategoryIdAndSeason() {
        executeDelete(localURL + "/match-days/categories/" + lastMatchDay.getCategoryId()
                + "/seasons/" + lastMatchDay.getSeason());
    }

    @When("^try to delete match days by categoryId, season and number")
    public void tryToDeleteMatchDaysByCategoryIdAndSeasonAndNumber() {
        executeDelete(localURL + "/match-days/categories/" + lastMatchDay.getCategoryId()
                + "/seasons/" + lastMatchDay.getSeason() + "/number/" + lastMatchDay.getMatchDayNumber());
    }

    @When("^try to create a new league with data$")
    public void tryToCreateANewLeague(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // Just one league per test.
        Map<String, String> row = rows.getFirst();
        LeagueDTO leagueDto = new LeagueDTO();
        leagueDto.setName(row.get("name"));
        leagueDto.setDescription(row.get("description"));
        leagueDto.setCategoryId(row.get("category_id"));
        leagueDto.setPublic(Boolean.parseBoolean(row.get("is_public")));
        leagueDto.setPassword(row.get("password"));
        leagueDto.setStatus(LeagueStatus.valueOf(row.get("status")));
        leagueDto.setMaxPlayers(row.get("max_players") != null ? Integer.parseInt(row.get("max_players")) : null);
        leagueDto.setLogoUrl(row.get("logo_url"));
        leagueDto.setPremium(Boolean.parseBoolean(row.get("is_premium")));
        leagueDto.setOwnerUsername(row.get("owner_username"));

        // Associate league players.
        String players = row.get("league_players");
        if (players != null && !players.isEmpty()) {
            List<LeaguePlayerDTO> playerList = Arrays.stream(players.split(";"))
                    .map(player -> {
                        String[] parts = player.split(",");
                        LeaguePlayerDTO playerDto = new LeaguePlayerDTO();
                        playerDto.setUsername(parts[0]);
                        playerDto.setTotalPoints(Integer.parseInt(parts[1]));
                        playerDto.setPlayerStatus(PlayerStatus.valueOf(parts[2]));
                        //  playerDto.setJoinDate(Long.parseLong(parts[3]));
                        return playerDto;
                    })
                    .collect(Collectors.toList());
            leagueDto.setLeaguePlayers(playerList);
        }

        try {
            executePost(localURL + "/leagues", objectMapper.writeValueAsString(leagueDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get all leagues$")
    public void tryToGetAllLeagues() {
        executeGet(localURL + "/leagues");
    }

    @When("^try to get a league by leagueId$")
    public void tryToGetALeagueByLeagueId() {
        executeGet(localURL + "/leagues/" + lastLeague.getLeagueId());
    }

    @When("^try to get a league by name$")
    public void tryToGetALeagueByName() {
        executeGet(localURL + "/leagues/names/" + lastLeague.getName());
    }

    @When("^try to close a league by leagueId$")
    public void tryToCloseALeagueByLeagueId() {
        executePut(localURL + "/leagues/close/" + lastLeague.getLeagueId());
    }

    @When("^try to close a league by name")
    public void tryToCloseALeagueByName() {
        executePut(localURL + "/leagues/close/names/" + lastLeague.getName());
    }

    @When("^try to delete a league by leagueId$")
    public void tryToDeleteALeagueByLeagueId() {
        executeDelete(localURL + "/leagues/" + lastLeague.getLeagueId());
    }

    @When("^try to delete a league by name")
    public void tryToDeleteALeagueByName() {
        executeDelete(localURL + "/leagues/names/" + lastLeague.getName());
    }

    @When("^try to create several league players with data$")
    public void tryToCreateSeveralLeaguePlayers(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        List<LeaguePlayerDTO> leaguePlayerDTOList = new ArrayList<>();

        for (Map<String, String> row : rows) {
            leaguePlayerDTOList.add(new LeaguePlayerDTO(
                    lastLeague.getLeagueId(),
                    row.get("username"),
                    Integer.parseInt(row.get("total_points")),
                    PlayerStatus.valueOf(row.get("status"))
            ));
        }

        try {
            executePost(localURL + "/league-players", objectMapper.writeValueAsString(leaguePlayerDTOList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get league players by leagueId$")
    public void tryToGetLeaguePlayersByLeagueId() {
        executeGet(localURL + "/league-players/leagues/" + lastLeague.getLeagueId());
    }

    @When("^try to get a league player by leagueId and username$")
    public void tryToGetLeaguePlayersByLeagueIdAndUsername() {
        executeGet(localURL + "/league-players/leagues/" + lastLeaguePlayer.getLeagueId()
                + "/players/" + lastLeaguePlayer.getUsername());
    }

    @When("^try to add (\\d+) points to player in league$")
    public void tryToAddPointsToPlayerInLeague(int points) {
        executePut(localURL + "/league-players/leagues/" + lastLeaguePlayer.getLeagueId()
                + "/players/" + lastLeaguePlayer.getUsername() + "?points=" + points);
    }

    @When("^try to delete league players by leagueId$")
    public void tryToDeleteLeaguePlayersByLeagueId() {
        executeGet(localURL + "/league-players/leagues/" + lastLeague.getLeagueId());
    }

    @When("^try to delete a league player by leagueId and username$")
    public void tryToDeleteLeaguePlayerByLeagueIdAndUsername() {
        executeDelete(localURL + "/league-players/leagues/" + lastLeaguePlayer.getLeagueId()
                + "/players/" + lastLeaguePlayer.getUsername());
    }

    @When("^try to get leagues by player username$")
    public void tryToGetLeaguesByPlayerUsername() {
        executeGet(localURL + "/league-players/players/" + lastLeaguePlayer.getUsername() + "/leagues");
    }

    @When("^try to create new league player points with data$")
    public void tryToCreateNewLeaguePlayerPoints(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // Just one league player points per test.
        Map<String, String> row = rows.getFirst();
        LeaguePlayerPointsDTO leaguePlayerPointsDto = new LeaguePlayerPointsDTO();
        leaguePlayerPointsDto.setCategoryId(row.get("category_id"));
        leaguePlayerPointsDto.setSeason(Integer.parseInt(row.get("season")));
        leaguePlayerPointsDto.setMatchDayNumber(Integer.parseInt(row.get("match_day_number")));
        leaguePlayerPointsDto.setLeagueId(lastLeague.getLeagueId());
        leaguePlayerPointsDto.setUsername(row.get("username"));
        leaguePlayerPointsDto.setPoints(Integer.parseInt(row.get("points")));

        try {
            executePost(localURL + "/league-players-points", objectMapper.writeValueAsString(leaguePlayerPointsDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @When("^try to get league player points by leagueId and username$")
    public void tryToGetLeaguePlayerPointsByLeagueIdAndUsername() {
        executeGet(localURL + "/league-player-points/leagues/" + lastLeague.getLeagueId()
                + "/players/" + lastLeaguePlayer.getUsername());
    }

    @When("^try to get league player points by categoryId, season and number")
    public void tryToGetLeaguePlayerPointsByCategoryIdSeasonAndNumber() {
        executeGet(localURL + "/league-player-points/categories/" + lastLeagueCategory.getCategoryId()
                + "/seasons/" + lastMatchDay.getSeason()
                + "/number/" + lastMatchDay.getMatchDayNumber());
    }

    @When("^try to delete league player points by leagueId and username$")
    public void tryToDeleteLeaguePlayerPointsByLeagueIdAndUsername() {
        executeDelete(localURL + "/league-player-points/leagues/" + lastLeague.getLeagueId()
                + "/players/" + lastLeaguePlayer.getUsername());
    }

    @When("^try to delete league player points by categoryId, season and number")
    public void tryToDeleteLeaguePlayerPointsByCategoryIdSeasonAndNumber() {
        executeDelete(localURL + "/league-player-points/categories/" + lastLeagueCategory.getCategoryId()
                + "/seasons/" + lastMatchDay.getSeason()
                + "/number/" + lastMatchDay.getMatchDayNumber());
    }

    @Then("a league category with categoryId (.*) has been stored successfully$")
    public void aLeagueCategoryHasBeenStored(String categoryId) {
        try {
            leaguesDbConnector = new LeaguesDatabaseConnector();
            lastLeagueCategory = leaguesDbConnector.getLeagueCategoryById(categoryId);
            assertNotNull(lastLeagueCategory);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("a match day with categoryId (.*), season (\\d+) and number (\\d+) has been stored successfully$")
    public void aMatchDayHasBeenStored(String categoryId, int season, int number) {
        try {
            leaguesDbConnector = new LeaguesDatabaseConnector();
            lastMatchDay = leaguesDbConnector.getMatchDayByCategoryIdSeasonAndMatchDayNumber(categoryId, season, number);
            assertNotNull(lastMatchDay);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("a league with name (.*) and status (.*) has been stored successfully$")
    public void aLeagueHasBeenStored(String name, String status) {
        try {
            leaguesDbConnector = new LeaguesDatabaseConnector();
            lastLeague = leaguesDbConnector.getLeagueByName(name);
            assertNotNull(lastLeague);
            assertEquals(status, lastLeague.getStatus().name());
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("a player with username (.*) has been associated to last league successfully$")
    public void aPlayerIsAssociatedToLastLeague(String username) {
        try {
            leaguesDbConnector = new LeaguesDatabaseConnector();
            assertNotNull(lastLeague);
            assertTrue(leaguesDbConnector.existsPlayerByLeagueId(lastLeague.getLeagueId(), username));

            lastLeaguePlayer = leaguesDbConnector.getLeaguePlayerByLeagueIdAndUsername(lastLeague.getLeagueId(), username);
        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Then("last player has (\\d+) points$")
    public void aPlayerHasPoints(int points) {
        assertEquals(points, lastLeaguePlayer.getTotalPoints().intValue());
    }

    @Then("a list of league categories with IDs (.*) are returned in response$")
    public void aListOfLeagueCategoriesWithIDsIsReturned(String categoryIds) throws JsonProcessingException {
        List<String> categoriesList = List.of(categoryIds.split(","));
        List<LeagueCategory> obtainedLeagueCategories = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<LeagueCategory>>() {
        });
        assertNotNull(obtainedLeagueCategories);
        for (String id : categoriesList) {
            assertTrue(obtainedLeagueCategories.stream().anyMatch(category -> category.getCategoryId().equals(id)));
        }
    }

    @Then("a list of match days with season (\\d+) and numbers (.*) are returned in response$")
    public void aListOfLeagueCategoriesWithIDsIsReturned(Integer season, String numbers) throws JsonProcessingException {
        List<Integer> numbersList = Stream.of(numbers.split(",")).map(Integer::parseInt).toList();
        List<MatchDayDTO> obtainedMatchDays = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<MatchDayDTO>>() {
        });
        assertNotNull(obtainedMatchDays);
        assertTrue(obtainedMatchDays.stream().allMatch(matchDay -> matchDay.getSeason().equals(season)));
        for (Integer number : numbersList) {
            assertTrue(obtainedMatchDays.stream().anyMatch(matchDay -> matchDay.getMatchDayNumber().equals(number)));
        }
    }

    @Then("a list of leagues with names (.*) are returned in response$")
    public void aListOfLeaguesWithNamesIsReturned(String names) throws JsonProcessingException {
        List<String> namesList = List.of(names.split(","));
        List<LeagueDTO> obtainedLeagues = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<LeagueDTO>>() {
        });
        assertNotNull(obtainedLeagues);
        for (String name : namesList) {
            assertTrue(obtainedLeagues.stream().anyMatch(league -> league.getName().equals(name)));
        }
    }

    @Then("a list of league players with usernames (.*) are returned in response$")
    public void aListOfLeaguePlayersWithUsernamesIsReturned(String usernames) throws JsonProcessingException {
        List<String> usernamesList = List.of(usernames.split(","));
        List<LeaguePlayerDTO> obtainedLeaguePlayers = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<LeaguePlayerDTO>>() {
        });
        assertNotNull(obtainedLeaguePlayers);
        for (String username : usernamesList) {
            assertTrue(obtainedLeaguePlayers.stream().anyMatch(player -> player.getUsername().equals(username)));
        }
    }

    @Then("a list of league player points with usernames (.*) are returned in response$")
    public void aListOfLeaguePlayerPointsWithUsernamesIsReturned(String usernames) throws JsonProcessingException {
        List<String> usernamesList = List.of(usernames.split(","));
        List<LeaguePlayerPointsDTO> obtainedLeaguePlayerPointsList = objectMapper.readValue(latestResponse.getBody(), new TypeReference<List<LeaguePlayerPointsDTO>>() {
        });
        assertNotNull(obtainedLeaguePlayerPointsList);
        for (String username : usernamesList) {
            assertTrue(obtainedLeaguePlayerPointsList.stream().anyMatch(info -> info.getUsername().equals(username)));
        }
    }

    @Then("a league category with ID (.*) is returned in response$")
    public void aLeagueCategoryWithIDIsReturned(String categoryId) throws JsonProcessingException {
        LeagueCategory obtainedLeagueCategory = objectMapper.readValue(latestResponse.getBody(), LeagueCategory.class);
        assertNotNull(obtainedLeagueCategory);
        assertEquals(categoryId, obtainedLeagueCategory.getCategoryId());
    }

    @Then("a league with name (.*) is returned in response$")
    public void aLeagueWithNameIsReturned(String name) throws JsonProcessingException {
        LeagueDTO obtainedLeague = objectMapper.readValue(latestResponse.getBody(), LeagueDTO.class);
        assertNotNull(obtainedLeague);
        assertEquals(name, obtainedLeague.getName());
    }

    @Then("a league player with username (.*) is returned in response$")
    public void aLeaguePlayerWithUsernameIsReturned(String username) throws JsonProcessingException {
        LeaguePlayerDTO obtainedLeaguePlayer = objectMapper.readValue(latestResponse.getBody(), LeaguePlayerDTO.class);
        assertNotNull(obtainedLeaguePlayer);
        assertEquals(username, obtainedLeaguePlayer.getUsername());
    }
}