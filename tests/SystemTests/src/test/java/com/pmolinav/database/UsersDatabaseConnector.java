package com.pmolinav.database;

import com.pmolinav.userslib.model.Role;
import com.pmolinav.userslib.model.User;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersDatabaseConnector {

    private static final String URL = "jdbc:postgresql://localhost:5432/users";
    private static final String USER = "postgres";
    private static final String PASSWORD = "mysecretpassword";

    private Connection connection;

    public UsersDatabaseConnector() throws SQLException {
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
        String query = "INSERT INTO users (username, password, name, email, creation_date, modification_date) "
                + "VALUES (?,?,?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set query params.
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setLong(5, user.getCreationDate());
            preparedStatement.setLong(6, user.getModificationDate());

            preparedStatement.executeUpdate();

            // Retrieve generated user_id
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Failed to retrieve user ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to insert user.", e);
        }
        for (Role role : user.getRoles()) {
            insertUserRoles(user.getUserId(), role.getRoleId());
        }
    }

    public void insertUserRoles(long userId, Long roleId) throws SQLException {
        String query = "INSERT INTO users_roles (user_id, role_id) VALUES (?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set query params.
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, roleId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to insert user roles.", e);
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT user_id, username, password, name, email, creation_date, modification_date" +
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
                Long dbCreationDate = resultSet.getLong("creation_date");
                Long dbModificationDate = resultSet.getLong("modification_date");

                return new User(dbUserId, dbUsername, dbPassword, dbName, dbEmail,
                        dbCreationDate, dbModificationDate, null);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to get user.", e);
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

    /*** ROLES  ***/

    public List<Role> getRolesByNames(List<String> roleNames) throws SQLException {
        if (CollectionUtils.isEmpty(roleNames)) {
            return Collections.emptyList();
        }
        String query = "SELECT role_id, name FROM roles WHERE name = ANY (?)";

        List<Role> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set query params.
            Array sqlArray = connection.createArrayOf("text", roleNames.toArray());
            preparedStatement.setArray(1, sqlArray);

            ResultSet resultSet = preparedStatement.executeQuery();
            // Extract data from result set.
            while (resultSet.next()) {
                long dbRoleId = resultSet.getLong("role_id");
                String dbName = resultSet.getString("name");

                result.add(new Role(dbRoleId, dbName));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to get roles.", e);
        }
    }

    public void insertRoles(List<String> roles) throws SQLException {
        String query = "INSERT INTO roles (role_id, name) VALUES (?,?)";

        for (int i = 0; i < roles.size(); i++) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Set query params.
                preparedStatement.setLong(1, i + 1);
                preparedStatement.setString(2, roles.get(i));
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Unexpected error occurred while trying to insert roles.", e);
            }
        }
    }

    public void deleteUsersRoles() throws SQLException {
        String query = "DELETE FROM users_roles";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete users_roles.", e);
        }
    }

    public void deleteRoles() throws SQLException {
        String query = "DELETE FROM roles";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete roles.", e);
        }
    }

    public void deleteAll() throws SQLException {
        try {
            deleteUsersRoles();
            deleteRoles();
            deleteUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unexpected error occurred while trying to delete all users data.", e);
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
//    public List<History> getHistoriesByEntityUserAndType(String entity, String user, String type) throws SQLException {
//        List<History> histories = new ArrayList<>();
//        String query = "SELECT id, change_details, change_type, create_user_id, creation_date, entity, entity_id" +
//                " FROM history WHERE entity = ? AND create_user_id = ? AND change_type = ?";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            // Set query params.
//            preparedStatement.setString(1, entity);
//            preparedStatement.setString(2, user);
//            preparedStatement.setString(3, type);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            // Extract data from result set.
//            while (resultSet.next()) {
//                long id = resultSet.getLong("id");
//                String dbChangeDetails = resultSet.getString("change_details");
//                String dbChangeType = resultSet.getString("change_type");
//                String dbCreateUserId = resultSet.getString("create_user_id");
//                Date dbCreationDate = resultSet.getDate("creation_date");
//                String dbEntity = resultSet.getString("entity");
//                String dbEntityId = resultSet.getString("entity_id");
//
//                histories.add(new History(dbCreationDate, ChangeType.valueOf(dbChangeType), dbEntity, dbEntityId,
//                        dbChangeDetails, dbCreateUserId));
//            }
//            return histories;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException("Unexpected error occurred while trying to get histories.", e);
//        }
//    }
}
