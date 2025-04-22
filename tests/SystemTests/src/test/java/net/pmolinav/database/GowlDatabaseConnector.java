package com.pmolinav.database;

import com.pmolinav.bookingslib.dto.ChangeType;
import com.pmolinav.bookingslib.model.Activity;
import com.pmolinav.bookingslib.model.Booking;
import com.pmolinav.bookingslib.model.History;
import com.pmolinav.bookingslib.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GowlDatabaseConnector {

    private static final String URL = "jdbc:postgresql://localhost:5432/gowl";
    private static final String USER = "postgres";
    private static final String PASSWORD = "mysecretpassword";

    private Connection connection;

    public GowlDatabaseConnector() throws SQLException {
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

    /*** USERS  ***/

    public void insertUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, password, name, email, role, creation_date, modification_date) "
                + "VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set query params.
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getRole());
            preparedStatement.setDate(6, new java.sql.Date(user.getCreationDate().getTime()));
            preparedStatement.setDate(7, new java.sql.Date(user.getModificationDate().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to insert user.", e);
        }
    }

    public void deleteUsers() throws SQLException {
        String query = "DELETE FROM users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete users.", e);
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT user_id, username, password, name, email, role, creation_date, modification_date" +
                " FROM users WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set query params.
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Extract data from result set.
            if (resultSet.next()) {
                long dbUserId = resultSet.getLong("user_id");
                String dbUsername = resultSet.getString("username");
                String dbPassword = resultSet.getString("password");
                String dbName = resultSet.getString("name");
                String dbEmail = resultSet.getString("email");
                String dbRole = resultSet.getString("role");
                Date dbCreationDate = resultSet.getDate("creation_date");
                Date dbModificationDate = resultSet.getDate("modification_date");

                return new User(dbUserId, dbUsername, dbPassword, dbName, dbEmail,
                        dbRole, dbCreationDate, dbModificationDate);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to get user.", e);
        }
    }

    /*** ACTIVITIES  ***/

    public void deleteActivities() throws SQLException {
        String query = "DELETE FROM activities";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete activities.", e);
        }
    }

    public Activity getActivityByName(String name) throws SQLException {
        String query = "SELECT activity_name, description, price, creation_date, modification_date" +
                " FROM activities WHERE activity_name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set query params.
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Extract data from result set.
            if (resultSet.next()) {
                String dbName = resultSet.getString("activity_name");
                String dbDescription = resultSet.getString("description");
                Integer dbPrice = resultSet.getInt("price");
                Date dbCreationDate = resultSet.getDate("creation_date");
                Date dbModificationDate = resultSet.getDate("modification_date");

                return new Activity(dbName, dbDescription, dbPrice, dbCreationDate, dbModificationDate);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to get activity.", e);
        }
    }

    /*** BOOKINGS  ***/

    public void deleteBookings() throws SQLException {
        String query = "DELETE FROM bookings";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete bookings.", e);
        }
    }

    public Booking getBookingByStatus(String status) throws SQLException {
        String query = "SELECT booking_id, user_id, activity_name, start_time, end_time, status, creation_date, modification_date" +
                " FROM bookings WHERE status = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set query params.
            preparedStatement.setString(1, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Extract data from result set.
            if (resultSet.next()) {
                long dbBookingId = resultSet.getLong("booking_id");
                long dbUserId = resultSet.getLong("user_id");
                String dbActivityName = resultSet.getString("activity_name");
                Date dbStartTime = resultSet.getDate("start_time");
                Date dbEndTime = resultSet.getDate("end_time");
                String dbStatus = resultSet.getString("status");
                Date dbCreationDate = resultSet.getDate("creation_date");
                Date dbModificationDate = resultSet.getDate("modification_date");

                return new Booking(dbBookingId, dbUserId, dbActivityName, dbStartTime, dbEndTime,
                        dbStatus, dbCreationDate, dbModificationDate);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to get booking.", e);
        }
    }

    /*** HISTORY  ***/

    public void deleteHistory() throws SQLException {
        String query = "DELETE FROM history";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete history table.", e);
        }
    }

    public List<History> getHistoriesByEntityUserAndType(String entity, String user, String type) throws SQLException {
        List<History> histories = new ArrayList<>();
        String query = "SELECT id, change_details, change_type, create_user_id, creation_date, entity, entity_id" +
                " FROM history WHERE entity = ? AND create_user_id = ? AND change_type = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set query params.
            preparedStatement.setString(1, entity);
            preparedStatement.setString(2, user);
            preparedStatement.setString(3, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Extract data from result set.
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String dbChangeDetails = resultSet.getString("change_details");
                String dbChangeType = resultSet.getString("change_type");
                String dbCreateUserId = resultSet.getString("create_user_id");
                Date dbCreationDate = resultSet.getDate("creation_date");
                String dbEntity = resultSet.getString("entity");
                String dbEntityId = resultSet.getString("entity_id");

                histories.add(new History(dbCreationDate, ChangeType.valueOf(dbChangeType), dbEntity, dbEntityId,
                        dbChangeDetails, dbCreateUserId));
            }
            return histories;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to get histories.", e);
        }
    }
}
