package com.pmolinav.database;

import com.pmolinav.predictionslib.dto.MatchStatus;
import com.pmolinav.predictionslib.dto.PlayerBetStatus;
import com.pmolinav.predictionslib.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictionsDatabaseConnector {

    private static final String URL = "jdbc:postgresql://localhost:5432/predictions";
    private static final String USER = "postgres";
    private static final String PASSWORD = "mysecretpassword";

    private Connection connection;

    public PredictionsDatabaseConnector() throws SQLException {
        connect();
    }

    private void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error while trying to connect to database.", e);
        }
    }

    /*** MATCHES  ***/

    public void insertMatches(List<Match> matches) throws SQLException {
        String query = "INSERT INTO match " +
                "(match_id, category_id, season, match_day_number, home_team, away_team, status, start_time, creation_date, modification_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        for (Match match : matches) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, match.getMatchId());
                preparedStatement.setString(2, match.getCategoryId());
                preparedStatement.setInt(3, match.getSeason());
                preparedStatement.setInt(4, match.getMatchDayNumber());
                preparedStatement.setString(5, match.getHomeTeam());
                preparedStatement.setString(6, match.getAwayTeam());
                preparedStatement.setString(7, match.getStatus().name());
                preparedStatement.setLong(8, match.getStartTime());
                if (match.getCreationDate() != null) {
                    preparedStatement.setLong(9, match.getCreationDate());
                } else {
                    preparedStatement.setNull(9, Types.BIGINT);
                }
                if (match.getModificationDate() != null) {
                    preparedStatement.setLong(10, match.getModificationDate());
                } else {
                    preparedStatement.setNull(10, Types.BIGINT);
                }

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Unexpected error occurred while trying to create match " + match, e);
            }
        }
    }

    public Match getMatchById(long matchId) throws SQLException {
        String query = "SELECT * FROM match WHERE match_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, matchId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Match(
                        rs.getLong("match_id"),
                        rs.getString("category_id"),
                        rs.getInt("season"),
                        rs.getInt("match_day_number"),
                        rs.getString("home_team"),
                        rs.getString("away_team"),
                        rs.getLong("start_time"),
                        MatchStatus.valueOf(rs.getString("status")),
                        rs.getLong("creation_date"),
                        rs.getLong("modification_date")
                );
            } else {
                return null;
            }
        }
    }

    public Match getMatchByHomeAndAwayTeams(String homeTeam, String awayTeam) throws SQLException {
        String query = "SELECT * FROM match WHERE home_team = ? AND away_team = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, homeTeam);
            statement.setString(2, awayTeam);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Match(
                        rs.getLong("match_id"),
                        rs.getString("category_id"),
                        rs.getInt("season"),
                        rs.getInt("match_day_number"),
                        rs.getString("home_team"),
                        rs.getString("away_team"),
                        rs.getLong("start_time"),
                        MatchStatus.valueOf(rs.getString("status")),
                        rs.getLong("creation_date"),
                        rs.getLong("modification_date")
                );
            } else {
                return null;
            }
        }
    }

    public List<Match> getMatchesByCategoryId(String categoryId) throws SQLException {
        String query = "SELECT * FROM match WHERE category_id = ?";
        List<Match> matches = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, categoryId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Match match = new Match(
                        rs.getLong("match_id"),
                        rs.getString("category_id"),
                        rs.getInt("season"),
                        rs.getInt("match_day_number"),
                        rs.getString("home_team"),
                        rs.getString("away_team"),
                        rs.getLong("start_time"),
                        MatchStatus.valueOf(rs.getString("status")),
                        rs.getLong("creation_date"),
                        rs.getLong("modification_date")
                );
                matches.add(match);
            }
        }

        return matches;
    }

    public void deleteMatches() throws SQLException {
        String query = "DELETE FROM match";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete matches.", e);
        }
    }

    /*** EVENTS  ***/

    public void insertEvents(List<Event> events) throws SQLException {
        String query = "INSERT INTO event " +
                "(event_type, description, creation_date, modification_date) " +
                "VALUES (?, ?, ?, ?)";

        for (Event event : events) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, event.getEventType());
                preparedStatement.setString(2, event.getDescription());

                if (event.getCreationDate() != null) {
                    preparedStatement.setLong(3, event.getCreationDate());
                } else {
                    preparedStatement.setNull(3, Types.BIGINT);
                }
                if (event.getModificationDate() != null) {
                    preparedStatement.setLong(4, event.getModificationDate());
                } else {
                    preparedStatement.setNull(4, Types.BIGINT);
                }

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Unexpected error occurred while trying to create event " + event, e);
            }
        }
    }

    public List<Event> getEventsByType(String type) throws SQLException {
        String query = "SELECT * FROM event WHERE event_type = ?";
        List<Event> events = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Event event = new Event(
                        rs.getString("event_type"),
                        rs.getString("description"),
                        rs.getLong("creation_date"),
                        rs.getLong("modification_date")
                );
                events.add(event);
            }
        }

        return events;
    }

    public void deleteEvents() throws SQLException {
        String query = "DELETE FROM event";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete events.", e);
        }
    }

    /*** ODDS  ***/

    public void insertOdds(List<Odds> oddsList) throws SQLException {
        String query = "INSERT INTO odds " +
                "(odds_id, match_id, event_type, label, value, active, creation_date, modification_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        for (Odds odds : oddsList) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, odds.getOddsId());
                preparedStatement.setLong(2, odds.getMatchId());
                preparedStatement.setString(3, odds.getEventType());
                preparedStatement.setString(4, odds.getLabel());
                preparedStatement.setBigDecimal(5, odds.getValue());
                preparedStatement.setBoolean(6, odds.getActive());

                if (odds.getCreationDate() != null) {
                    preparedStatement.setLong(7, odds.getCreationDate());
                } else {
                    preparedStatement.setNull(7, Types.BIGINT);
                }
                if (odds.getModificationDate() != null) {
                    preparedStatement.setLong(8, odds.getModificationDate());
                } else {
                    preparedStatement.setNull(8, Types.BIGINT);
                }

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Unexpected error occurred while trying to create odds " + odds, e);
            }
        }
    }

    public List<Odds> getOddsByLabel(String label) throws SQLException {
        String query = "SELECT * FROM odds WHERE label = ?";
        List<Odds> oddsList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, label);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Odds odds = new Odds(
                        rs.getLong("odds_id"),
                        rs.getString("event_type"),
                        rs.getLong("match_id"),
                        rs.getString("label"),
                        rs.getBigDecimal("value"),
                        rs.getBigDecimal("point"),
                        rs.getString("provider"),
                        rs.getBoolean("active"),
                        rs.getLong("creation_date"),
                        rs.getLong("modification_date")
                );
                oddsList.add(odds);
            }
        }

        return oddsList;
    }

    public void deleteOdds() throws SQLException {
        String query = "DELETE FROM odds";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete odds.", e);
        }
    }

    /*** PLAYER BET  ***/

    public void insertPlayerBets(List<PlayerBet> playerBets) throws SQLException {
        String query = "INSERT INTO player_bet " +
                "(bet_id, username, match_id, total_stake, status, creation_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        for (PlayerBet bet : playerBets) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, bet.getBetId());
                preparedStatement.setString(2, bet.getUsername());
                preparedStatement.setLong(3, bet.getMatchId());
                preparedStatement.setBigDecimal(4, bet.getTotalStake());
                preparedStatement.setString(5, bet.getStatus().name());

                if (bet.getCreationDate() != null) {
                    preparedStatement.setLong(6, bet.getCreationDate());
                } else {
                    preparedStatement.setNull(6, Types.BIGINT);
                }

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Unexpected error occurred while trying to create player bet " + bet, e);
            }
        }
    }

    public List<PlayerBet> getPlayerBetsByUsername(String username) throws SQLException {
        String query = "SELECT * FROM player_bet WHERE username = ?";
        List<PlayerBet> playerBets = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                PlayerBet bet = new PlayerBet(
                        rs.getLong("bet_id"),
                        rs.getString("username"),
                        rs.getLong("match_id"),
                        rs.getLong("league_id"),
                        rs.getBigDecimal("total_stake"),
                        PlayerBetStatus.valueOf(rs.getString("status")),
                        rs.getLong("creation_date")
                );
                playerBets.add(bet);
            }
        }

        return playerBets;
    }

    public List<PlayerBet> getPlayerBetsByUsernameAndMatchId(String username, Long matchId) throws SQLException {
        String query = "SELECT * FROM player_bet WHERE username = ? AND match_id = ?";
        List<PlayerBet> playerBets = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setLong(2, matchId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                long betId = rs.getLong("bet_id");

                PlayerBet bet = new PlayerBet(
                        betId,
                        rs.getString("username"),
                        rs.getLong("match_id"),
                        rs.getLong("league_id"),
                        rs.getBigDecimal("total_stake"),
                        PlayerBetStatus.valueOf(rs.getString("status")),
                        rs.getLong("creation_date")
                );

                // Get player bet selections.
                bet.setSelections(getSelectionsByBetId(betId));

                playerBets.add(bet);
            }
        }

        return playerBets;
    }

    public Map<String, BigDecimal> getTotalWonStakeMultipliedByUser() throws SQLException {
        String query = "SELECT username, SUM(total_stake * 100) AS total_won_stake_multiplied " +
                "FROM player_bet WHERE status = 'WON' GROUP BY username";

        Map<String, BigDecimal> result = new HashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                BigDecimal total = rs.getBigDecimal("total_won_stake_multiplied");
                result.put(username, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error while retrieving total WON stake multiplied by user.", e);
        }

        return result;
    }


    private List<PlayerBetSelection> getSelectionsByBetId(long betId) throws SQLException {
        String selectionQuery = "SELECT * FROM player_bet_selection WHERE bet_id = ?";
        List<PlayerBetSelection> selections = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(selectionQuery)) {
            stmt.setLong(1, betId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PlayerBetSelection selection = new PlayerBetSelection(
                        rs.getLong("selection_id"),
                        rs.getLong("bet_id"),
                        rs.getString("event_type"),
                        rs.getLong("odds_id"),
                        rs.getBigDecimal("stake"),
                        PlayerBetStatus.valueOf(rs.getString("status")),
                        rs.getLong("creation_date")
                );
                selections.add(selection);
            }
        }

        return selections;
    }


    public void deletePlayerBets() throws SQLException {
        String query = "DELETE FROM player_bet";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete player bets.", e);
        }
    }

    /*** PLAYER BET SELECTION  ***/

    public void insertPlayerBetSelections(List<PlayerBetSelection> selections) throws SQLException {
        String query = "INSERT INTO player_bet_selection " +
                "(bet_id, odds_id, stake, status, creation_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        for (PlayerBetSelection selection : selections) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, selection.getBetId());
                preparedStatement.setLong(2, selection.getOddsId());
                preparedStatement.setBigDecimal(3, selection.getStake());
                preparedStatement.setString(4, selection.getStatus().name());

                if (selection.getCreationDate() != null) {
                    preparedStatement.setLong(5, selection.getCreationDate());
                } else {
                    preparedStatement.setNull(5, Types.BIGINT);
                }

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Unexpected error occurred while trying to create player bet selection " + selection, e);
            }
        }
    }

    public PlayerBetSelection getPlayerBetsSelectionByPlayerBetId(Long betId) throws SQLException {
        String query = "SELECT * FROM player_bet_selection WHERE bet_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, betId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new PlayerBetSelection(
                        rs.getLong("selection_id"),
                        rs.getLong("bet_id"),
                        rs.getString("event_type"),
                        rs.getLong("odds_id"),
                        rs.getBigDecimal("stake"),
                        PlayerBetStatus.valueOf(rs.getString("status")),
                        rs.getLong("creation_date")
                );
            }
        }

        return null;
    }

    public void deletePlayerBetSelections() throws SQLException {
        String query = "DELETE FROM player_bet_selection";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete player bet selections.", e);
        }
    }

    public void deleteAll() throws SQLException {
        try {
            deletePlayerBetSelections();
            deletePlayerBets();
            deleteOdds();
            deleteEvents();
            deleteMatches();
            deleteMappings();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete all prediction data.", e);
        }
    }

    /*** MAPPINGS  ***/

    public void insertMappings(List<ExternalCategoryMapping> mappings) throws SQLException {
        String query = "INSERT INTO external_category_mapping " +
                "(category_id, external_sport_key) " +
                "VALUES (?, ?)";

        for (ExternalCategoryMapping mapping : mappings) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, mapping.getCategoryId());
                preparedStatement.setString(2, mapping.getExternalSportKey());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Unexpected error occurred while trying to create mapping " + mapping, e);
            }
        }
    }

    public void deleteMappings() throws SQLException {
        String query = "DELETE FROM external_category_mapping";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete mappings.", e);
        }
    }
}
