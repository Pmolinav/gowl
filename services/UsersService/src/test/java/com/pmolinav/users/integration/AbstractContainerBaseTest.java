package com.pmolinav.users.integration;

import com.pmolinav.users.auth.SpringSecurityConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Docker Environment is needed to run integration tests.
 **/
@ActiveProfiles("test")
public abstract class AbstractContainerBaseTest {
    @Value("${database.port}")
    private int databasePort;
    private final static int DB_PORT = 5432;
    static final PostgreSQLContainer<?> postgresContainer;
    //    static final KafkaContainer kafkaContainer;
    static String jdbcUrl;
    static String username;
    static String password;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withExposedPorts(DB_PORT)
                .withDatabaseName("users")
                .withUsername("postgres")
                .withPassword("mysecretpassword");

        postgresContainer.start();

//        kafkaContainer =
//                new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
//        kafkaContainer.start();

        jdbcUrl = postgresContainer.getJdbcUrl();
        username = postgresContainer.getUsername();
        password = postgresContainer.getPassword();

        System.setProperty("database.port", String.valueOf(postgresContainer.getMappedPort(DB_PORT)));

        System.out.println("Connection started for database: " + postgresContainer.getDatabaseName() +
                " and mapped port: " + postgresContainer.getMappedPort(DB_PORT));
    }

    @BeforeEach
    public void givenEmptyTablesBeforeTests() {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try (Statement statement = connection.createStatement()) {
                String deleteUsersRolesQuery = "DELETE FROM users_roles;";
                statement.executeUpdate(deleteUsersRolesQuery);

                String deleteRolesQuery = "DELETE FROM roles;";
                statement.executeUpdate(deleteRolesQuery);

                String deleteUsersQuery = "DELETE FROM users;";
                statement.executeUpdate(deleteUsersQuery);
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    protected void givenSomePreviouslyStoredDataWithId(long idUser) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try (Statement statement = connection.createStatement()) {
                String insertRoleUserQuery = "INSERT INTO roles (role_id, name) VALUES (" + 1 + ", 'ROLE_USER');";
                statement.executeUpdate(insertRoleUserQuery);

                String insertRolePremiumQuery = "INSERT INTO roles (role_id, name) VALUES (" + 2 + ", 'ROLE_PREMIUM');";
                statement.executeUpdate(insertRolePremiumQuery);

                String insertRoleAdminQuery = "INSERT INTO roles (role_id, name) VALUES (" + 3 + ", 'ROLE_ADMIN');";
                statement.executeUpdate(insertRoleAdminQuery);

                String insertUserQuery = "INSERT INTO users (user_id, username, password, name, email, creation_date, modification_date) " +
                        "VALUES (" + idUser + ", 'someUser', '" + SpringSecurityConfig.passwordEncoder().encode("somePassword") + "', 'John Doe', 'john@example.com', '12345', NULL);";
                statement.executeUpdate(insertUserQuery);

                String insertUserQuery2 = "INSERT INTO users (user_id, username, password, name, email, creation_date, modification_date) " +
                        "VALUES (" + (idUser + 1) + ", 'otherUser', '" + SpringSecurityConfig.passwordEncoder().encode("somePassword") + "', 'Jane Smith', 'jane@example.com', '123456', '1234567');";
                statement.executeUpdate(insertUserQuery2);

                String insertUserRoleQuery = "INSERT INTO users_roles (user_id, role_id) VALUES (" + idUser + ", " + 3 + ");";
                statement.executeUpdate(insertUserRoleQuery);

                String insertUserRoleQuery2 = "INSERT INTO users_roles (user_id, role_id) VALUES (" + (idUser + 1) + ", " + 3 + ");";
                statement.executeUpdate(insertUserRoleQuery2);
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}

