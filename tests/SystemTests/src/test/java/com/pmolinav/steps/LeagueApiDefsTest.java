package com.pmolinav.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.LeagueStatus;
import com.pmolinav.leagueslib.model.PlayerStatus;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

public class LeagueApiDefsTest extends BaseSystemTest {

    private final String localURL = "http://localhost:8006";

    @When("^try to get LeagueApi health")
    public void tryToGetHealth() {
        executeGet(localURL + "/health");
    }

    @When("^try to get all league categories with public endpoint$")
    public void tryToGetAllLeagueCategoriesPublicEndpoint() {
        executeGet(localURL + "/categories");
    }

    @When("^try to get a league category by categoryId (\\w+) with public endpoint$")
    public void tryToGetALeagueCategoryByCategoryIdPublicEndpoint(String categoryId) {
        executeGet(localURL + "/categories/" + categoryId);
    }

    @When("^try to get match days by categoryId (\\w+) and season (\\d+) with public endpoint$")
    public void tryToGetMatchDaysByCategoryIdAndSeasonPublicEndpoint(String categoryId, int season) {
        executeGet(localURL + "/match-days/categories/" + categoryId
                + "/seasons/" + season);
    }

    @When("^try to create a new league with public endpoint with data$")
    public void tryToCreateANewLeaguePublicEndpoint(DataTable dataTable) {
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

    @When("^try to get a league by leagueId with public endpoint$")
    public void tryToGetALeagueByLeagueIdPublicEndpoint() {
        executeGet(localURL + "/leagues/" + lastLeague.getLeagueId());
    }

    @When("^try to get a league by name (.*) with public endpoint$")
    public void tryToGetALeagueByNamePublicEndpoint(String name) {
        executeGet(localURL + "/leagues/names/" + name);
    }

    @When("^try to close a league by leagueId with public endpoint$")
    public void tryToCloseALeagueByLeagueIdPublicEndpoint() {
        executePut(localURL + "/leagues/close/" + lastLeague.getLeagueId());
    }

    @When("^try to close a league by name (.*) with public endpoint$")
    public void tryToCloseALeagueByNamePublicEndpoint(String name) {
        executePut(localURL + "/leagues/close/names/" + name);
    }

    @When("^try to create several league players with public endpoint with data$")
    public void tryToCreateSeveralLeaguePlayersPublicEndpoint(DataTable dataTable) {
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

    @When("^try to get league players by leagueId with public endpoint$")
    public void tryToGetLeaguePlayersByLeagueIdPublicEndpoint() {
        executeGet(localURL + "/league-players/leagues/" + lastLeague.getLeagueId());
    }

    @When("^try to get a league player by leagueId and username (\\w+) with public endpoint$")
    public void tryToGetLeaguePlayersByLeagueIdAndUsernamePublicEndpoint(String username) {
        executeGet(localURL + "/league-players/leagues/" + lastLeaguePlayer.getLeagueId()
                + "/players/" + username);
    }

    @When("^try to delete a league player by leagueId and username (\\w+) with public endpoint$")
    public void tryToDeleteLeaguePlayerByLeagueIdAndUsernamePublicEndpoint(String username) {
        executeDelete(localURL + "/league-players/leagues/" + lastLeaguePlayer.getLeagueId()
                + "/players/" + username);
    }

    @When("^try to get leagues by player username (\\w+) with public endpoint$")
    public void tryToGetLeaguesByPlayerUsernamePublicEndpoint(String username) {
        executeGet(localURL + "/league-players/players/" + username + "/leagues");
    }

    @When("^try to get league player points by leagueId and username (\\w+) with public endpoint$")
    public void tryToGetLeaguePlayerPointsByLeagueIdAndUsernamePublicEndpoint(String username) {
        executeGet(localURL + "/league-player-points/leagues/" + lastLeague.getLeagueId()
                + "/players/" + username);
    }

    @When("^try to get league player points by categoryId (\\w+), season (\\d+) and number (\\d+)$")
    public void tryToGetLeaguePlayerPointsByCategoryIdSeasonAndNumberPublicEndpoint(String categoryId, int season, int number) {
        executeGet(localURL + "/league-player-points/categories/" + categoryId
                + "/seasons/" + season + "/number/" + number);
    }
}