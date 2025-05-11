package com.pmolinav.leagues.integration;

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
        postgresContainer = new PostgreSQLContainer<>("postgres:15.0")
                .withExposedPorts(DB_PORT)
                .withDatabaseName("leagues")
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
                String deleteLeagueCategoriesQuery = "DELETE FROM league_category;";
                statement.executeUpdate(deleteLeagueCategoriesQuery);
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    protected void givenSomePreviouslyStoredLeagueCategoryWithId(String categoryId) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try (Statement statement = connection.createStatement()) {
                String insertLeagueCategoryQuery = "INSERT INTO league_category (category_id, name, description, sport, country, icon_url, is_active, creation_date, modification_date) " +
                        "VALUES ('" + categoryId + "', 'Some League Category', 'Some Description', 'FOOTBALL', 'ES', NULL, true, 1, 1);";
                statement.executeUpdate(insertLeagueCategoryQuery);
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}

