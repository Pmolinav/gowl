package com.pmolinav.database;

import com.pmolinav.leagueslib.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaguesDatabaseConnector {

    private static final String URL = "jdbc:postgresql://localhost:5432/leagues";
    private static final String USER = "postgres";
    private static final String PASSWORD = "mysecretpassword";

    private Connection connection;

    public LeaguesDatabaseConnector() throws SQLException {
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

    /*** LEAGUES  ***/

    public void insertLeagues(List<League> leagues) throws SQLException {
        String leagueQuery = "INSERT INTO leagues " +
                "(name, description, category_id, is_public, password, status, max_players, " +
                "logo_url, is_premium, owner_username, creation_date, modification_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String leaguePlayerQuery = "INSERT INTO league_player " +
                "(league_id, username, total_points, player_status, join_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        for (League league : leagues) {
            try (PreparedStatement leagueStatement = connection.prepareStatement(leagueQuery, Statement.RETURN_GENERATED_KEYS)) {

                // Insert league
                leagueStatement.setString(1, league.getName());
                leagueStatement.setString(2, league.getDescription());
                leagueStatement.setString(3, league.getCategoryId());
                leagueStatement.setBoolean(4, league.isPublic());
                leagueStatement.setString(5, league.getPassword());
                leagueStatement.setString(6, league.getStatus().name());
                if (league.getMaxPlayers() != null) {
                    leagueStatement.setInt(7, league.getMaxPlayers());
                } else {
                    leagueStatement.setNull(7, Types.INTEGER);
                }
                leagueStatement.setString(8, league.getLogoUrl());
                leagueStatement.setBoolean(9, league.isPremium());
                leagueStatement.setString(10, league.getOwnerUsername());
                leagueStatement.setLong(11, league.getCreationDate());
                if (league.getModificationDate() != null) {
                    leagueStatement.setLong(12, league.getModificationDate());
                } else {
                    leagueStatement.setNull(12, Types.BIGINT);
                }

                leagueStatement.executeUpdate();

                // Get generated leagueId
                try (ResultSet generatedKeys = leagueStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long leagueId = generatedKeys.getLong(1);

                        // Insert leaguePlayers if present
                        if (league.getLeaguePlayers() != null) {
                            for (LeaguePlayer player : league.getLeaguePlayers()) {
                                try (PreparedStatement playerStatement = connection.prepareStatement(leaguePlayerQuery)) {
                                    playerStatement.setLong(1, leagueId);
                                    playerStatement.setString(2, player.getUsername());
                                    playerStatement.setInt(3, player.getTotalPoints());
                                    playerStatement.setString(4, player.getPlayerStatus().name());
                                    playerStatement.setLong(5, player.getJoinDate());
                                    playerStatement.executeUpdate();
                                }
                            }
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Unexpected error while inserting league: " + league.getName(), e);
            }
        }
    }


    public League getLeagueByName(String name) throws SQLException {
        String query = "SELECT league_id, name, description, category_id, is_public, password, status, " +
                "max_players, logo_url, is_premium, owner_username, creation_date, modification_date " +
                "FROM league WHERE name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                League league = new League();
                league.setLeagueId(resultSet.getLong("league_id"));
                league.setName(resultSet.getString("name"));
                league.setDescription(resultSet.getString("description"));
                league.setCategoryId(resultSet.getString("category_id"));
                league.setPublic(resultSet.getBoolean("is_public"));
                league.setPassword(resultSet.getString("password"));
                league.setStatus(LeagueStatus.valueOf(resultSet.getString("status")));
                int maxPlayers = resultSet.getInt("max_players");
                if (!resultSet.wasNull()) {
                    league.setMaxPlayers(maxPlayers);
                }
                league.setLogoUrl(resultSet.getString("logo_url"));
                league.setPremium(resultSet.getBoolean("is_premium"));
                league.setOwnerUsername(resultSet.getString("owner_username"));
                league.setCreationDate(resultSet.getLong("creation_date"));
                long modificationDate = resultSet.getLong("modification_date");
                if (!resultSet.wasNull()) {
                    league.setModificationDate(modificationDate);
                }

                return league;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to get league by name: " + name, e);
        }
    }

    public LeaguePlayer getLeaguePlayerByLeagueIdAndUsername(long leagueId, String username) throws SQLException {
        String query = "SELECT league_id, username, total_points, player_status, join_date " +
                "FROM league_player WHERE league_id = ? AND username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, leagueId);
            preparedStatement.setString(2, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                LeaguePlayer leaguePlayer = new LeaguePlayer();
                leaguePlayer.setLeagueId(resultSet.getLong("league_id"));
                leaguePlayer.setUsername(resultSet.getString("username"));
                leaguePlayer.setTotalPoints(resultSet.getInt("total_points"));
                leaguePlayer.setPlayerStatus(PlayerStatus.valueOf(resultSet.getString("player_status")));
                leaguePlayer.setJoinDate(resultSet.getLong("join_date"));

                return leaguePlayer;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to find LeaguePlayer in league.", e);
        }
    }

    public boolean existsPlayerByLeagueId(long leagueId, String username) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM league_player WHERE league_id = ? AND username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, leagueId);
            preparedStatement.setString(2, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to find player in league.", e);
        }
    }

    public void deleteLeagues() throws SQLException {
        String query = "DELETE FROM league";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete leagues.", e);
        }
    }

    /*** LEAGUE CATEGORIES  ***/

    public void insertCategories(List<LeagueCategory> categories) throws SQLException {
        String query = "INSERT INTO league_category " +
                "(category_id, name, description, sport, country, icon_url," +
                " is_active, creation_date, modification_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        for (LeagueCategory category : categories) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, category.getCategoryId());
                preparedStatement.setString(2, category.getName());
                preparedStatement.setString(3, category.getDescription());
                preparedStatement.setString(4, category.getSport());
                preparedStatement.setString(5, category.getCountry());

                if (category.getModificationDate() != null) {
                    preparedStatement.setString(6, category.getIconUrl());
                } else {
                    preparedStatement.setNull(6, Types.VARCHAR);
                }
                preparedStatement.setBoolean(7, category.isActive());
                preparedStatement.setLong(8, category.getCreationDate());

                if (category.getModificationDate() != null) {
                    preparedStatement.setLong(9, category.getModificationDate());
                } else {
                    preparedStatement.setNull(9, Types.BIGINT);
                }

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Unexpected error occurred while trying to insert category: " + category.getCategoryId(), e);
            }
        }
    }

    public LeagueCategory getLeagueCategoryById(String categoryId) throws SQLException {
        String query = "SELECT category_id, name, description, sport, country, icon_url, " +
                "is_active, creation_date, modification_date " +
                "FROM league_category WHERE category_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, categoryId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                LeagueCategory category = new LeagueCategory();
                category.setCategoryId(resultSet.getString("category_id"));
                category.setName(resultSet.getString("name"));
                category.setDescription(resultSet.getString("description"));
                category.setSport(resultSet.getString("sport"));
                category.setCountry(resultSet.getString("country"));
                category.setIconUrl(resultSet.getString("icon_url"));
                category.setActive(resultSet.getBoolean("is_active"));
                category.setCreationDate(resultSet.getLong("creation_date"));
                long modificationDate = resultSet.getLong("modification_date");
                if (!resultSet.wasNull()) {
                    category.setModificationDate(modificationDate);
                }
                return category;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error while getting LeagueCategory by ID: " + categoryId, e);
        }
    }

    public void deleteCategories() throws SQLException {
        String query = "DELETE FROM league_category";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete league categories.", e);
        }
    }

    /*** MATCH DAYS  ***/

    public void insertMatchDays(List<MatchDay> matchDays) throws SQLException {
        String query = "INSERT INTO match_day " +
                "(category_id, season, match_day_number, start_date, end_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        for (MatchDay matchDay : matchDays) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, matchDay.getCategoryId());
                preparedStatement.setInt(2, matchDay.getSeason());
                preparedStatement.setInt(3, matchDay.getMatchDayNumber());
                preparedStatement.setLong(4, matchDay.getStartDate());
                preparedStatement.setLong(5, matchDay.getEndDate());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Unexpected error occurred while trying to insert match day: " +
                        "CategoryId=" + matchDay.getCategoryId() +
                        ", Season=" + matchDay.getSeason() +
                        ", MatchDayNumber=" + matchDay.getMatchDayNumber(), e);
            }
        }
    }

    public MatchDay getMatchDayByCategoryIdSeasonAndMatchDayNumber(String categoryId, int season, int matchDayNumber) throws SQLException {
        String query = "SELECT category_id, season, match_day_number, start_date, end_date " +
                "FROM match_day WHERE category_id = ? AND season = ? AND match_day_number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, categoryId);
            preparedStatement.setInt(2, season);
            preparedStatement.setInt(3, matchDayNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                MatchDay matchDay = new MatchDay();
                matchDay.setCategoryId(resultSet.getString("category_id"));
                matchDay.setSeason(resultSet.getInt("season"));
                matchDay.setMatchDayNumber(resultSet.getInt("match_day_number"));
                matchDay.setStartDate(resultSet.getLong("start_date"));
                matchDay.setEndDate(resultSet.getLong("end_date"));

                return matchDay;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error while getting MatchDay for categoryId: " + categoryId +
                    ", season: " + season + ", matchDayNumber: " + matchDayNumber, e);
        }
    }

    public List<MatchDay> getMatchDaysByCategoryId(String categoryId) throws SQLException {
        String query = "SELECT category_id, season, match_day_number, start_date, end_date " +
                "FROM match_day WHERE category_id = ?";

        List<MatchDay> matchDays = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, categoryId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                MatchDay matchDay = new MatchDay();
                matchDay.setCategoryId(resultSet.getString("category_id"));
                matchDay.setSeason(resultSet.getInt("season"));
                matchDay.setMatchDayNumber(resultSet.getInt("match_day_number"));
                matchDay.setStartDate(resultSet.getLong("start_date"));
                matchDay.setEndDate(resultSet.getLong("end_date"));

                matchDays.add(matchDay);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error while getting MatchDays for categoryId: " + categoryId, e);
        }

        return matchDays;
    }

    public void deleteMatchDays() throws SQLException {
        String query = "DELETE FROM match_day";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete match days.", e);
        }
    }

    /*** LEAGUE PLAYERS  ***/

    public void deleteLeaguePlayers() throws SQLException {
        String query = "DELETE FROM league_player";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete league players.", e);
        }
    }

    /*** LEAGUE PLAYER POINTS  ***/

    public void deleteLeaguePlayerPoints() throws SQLException {
        String query = "DELETE FROM league_player_points";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete league player points.", e);
        }
    }

    /*** HISTORY  ***/

//    public void deleteHistory() throws SQLException {
//        String query = "DELETE FROM history";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException("Unexpected error occurred while trying to delete history table.", e);
//        }
//    }
//
//    public List<History> getHistoriesByEntityLeagueAndType(String entity, String league, String type) throws SQLException {
//        List<History> histories = new ArrayList<>();
//        String query = "SELECT id, change_details, change_type, create_league_id, creation_date, entity, entity_id" +
//                " FROM history WHERE entity = ? AND create_league_id = ? AND change_type = ?";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            // Set query params.
//            preparedStatement.setString(1, entity);
//            preparedStatement.setString(2, league);
//            preparedStatement.setString(3, type);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            // Extract data from result set.
//            while (resultSet.next()) {
//                long id = resultSet.getLong("id");
//                String dbChangeDetails = resultSet.getString("change_details");
//                String dbChangeType = resultSet.getString("change_type");
//                String dbCreateLeagueId = resultSet.getString("create_league_id");
//                Date dbCreationDate = resultSet.getDate("creation_date");
//                String dbEntity = resultSet.getString("entity");
//                String dbEntityId = resultSet.getString("entity_id");
//
//                histories.add(new History(dbCreationDate, ChangeType.valueOf(dbChangeType), dbEntity, dbEntityId,
//                        dbChangeDetails, dbCreateLeagueId));
//            }
//            return histories;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException("Unexpected error occurred while trying to get histories.", e);
//        }
//    }
}
