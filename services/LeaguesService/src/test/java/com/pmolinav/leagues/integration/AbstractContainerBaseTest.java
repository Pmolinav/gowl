package com.pmolinav.leagues.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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
    //    static final KafkaContainer kafkaContainer;
    static String jdbcUrl;
    static String username;
    static String password;

    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.0")
            .withDatabaseName("leagues")
            .withUsername("postgres")
            .withPassword("mysecretpassword");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        postgresContainer.start();

        jdbcUrl = postgresContainer.getJdbcUrl();
        username = postgresContainer.getUsername();
        password = postgresContainer.getPassword();

        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    public void givenEmptyTablesBeforeTests() {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try (Statement statement = connection.createStatement()) {
                String deleteLeagueCategoriesQuery = "DELETE FROM league_category;";
                statement.executeUpdate(deleteLeagueCategoriesQuery);
                String deleteMatchDaysQuery = "DELETE FROM match_day;";
                statement.executeUpdate(deleteMatchDaysQuery);
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    protected void givenSomePreviouslyStoredLeagueCategoryWithId(String categoryId) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try (Statement statement = connection.createStatement()) {
                String insertLeagueCategoryQuery = "INSERT INTO league_category (category_id, name, description, sport, country, icon_url, is_active, creation_date, modification_date) " +
                        "VALUES ('" + categoryId + "', 'Some League Category', 'Some Description', 'FOOTBALL', 'ES', NULL, true, 1, 1) ON CONFLICT DO NOTHING;";
                statement.executeUpdate(insertLeagueCategoryQuery);
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }

    protected void givenSomePreviouslyStoredMatchDayWithId(String categoryId, Integer season, Integer matchDayNumber) {
        givenSomePreviouslyStoredLeagueCategoryWithId(categoryId);
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try (Statement statement = connection.createStatement()) {
                String insertMatchDayQuery = "INSERT INTO match_day (category_id, season, match_day_number, start_date, end_date) " +
                        "VALUES ('" + categoryId + "', " + season + ", " + matchDayNumber + ", 123, 12345);";
                statement.executeUpdate(insertMatchDayQuery);
            }
        } catch (Exception e) {
            Assertions.fail();
        }
    }
}

