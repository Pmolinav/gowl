package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagues.repositories.LeagueCategoryRepository;
import com.pmolinav.leagues.repositories.LeaguePlayerRepository;
import com.pmolinav.leagues.repositories.LeagueRepository;
import com.pmolinav.leagues.repositories.MatchDayRepository;
import com.pmolinav.leagueslib.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Docker Environment is needed to run integration tests.
 **/
@ActiveProfiles("test")
public abstract class AbstractContainerBaseTest {
    @Value("${database.port}")
    private int databasePort;
    private final static int DB_PORT = 5432;
    protected static long leagueId;
    //    static final KafkaContainer kafkaContainer;
    static String jdbcUrl;
    static String username;
    static String password;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected LeagueCategoryRepository leagueCategoryRepository;
    @Autowired
    protected LeaguePlayerRepository leaguePlayerRepository;
    @Autowired
    protected LeagueRepository leagueRepository;
    @Autowired
    protected MatchDayRepository matchDayRepository;
    @Autowired
    protected final ObjectMapper objectMapper = new ObjectMapper();

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
        try {
            leagueCategoryRepository.deleteAll();
            matchDayRepository.deleteAll();
            leagueRepository.deleteAll();
            leaguePlayerRepository.deleteAll();
        } catch (Exception e) {
            fail();
        }
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
//            try (Statement statement = connection.createStatement()) {
//                String deleteLeagueCategoriesQuery = "DELETE FROM league_category;";
//                statement.executeUpdate(deleteLeagueCategoriesQuery);
//                String deleteMatchDaysQuery = "DELETE FROM match_day;";
//                statement.executeUpdate(deleteMatchDaysQuery);
//                String deleteLeaguesQuery = "DELETE FROM league;";
//                statement.executeUpdate(deleteLeaguesQuery);
//                String deleteLeaguePlayersQuery = "DELETE FROM league_player;";
//                statement.executeUpdate(deleteLeaguePlayersQuery);
//            }
//        } catch (Exception e) {
//            Assertions.fail();
//        }
    }

    protected void givenSomePreviouslyStoredLeagueCategoryWithId(String categoryId) {
        try {
            leagueCategoryRepository.save(new LeagueCategory(categoryId, "Some League Category",
                    "Some Description", "FOOTBALL", "ES", null,
                    true, 1L, 1L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
//            try (Statement statement = connection.createStatement()) {
//                String insertLeagueCategoryQuery = "INSERT INTO league_category (category_id, name, description, sport, country, icon_url, is_active, creation_date, modification_date) " +
//                        "VALUES ('" + categoryId + "', 'Some League Category', 'Some Description', 'FOOTBALL', 'ES', NULL, true, 1, 1) ON CONFLICT DO NOTHING;";
//                statement.executeUpdate(insertLeagueCategoryQuery);
//            }
//        } catch (Exception e) {
//            fail();
//        }
    }

    protected void givenSomePreviouslyStoredMatchDayWithId(String categoryId, Integer season, Integer matchDayNumber) {
        givenSomePreviouslyStoredLeagueCategoryWithId(categoryId);
        try {
            matchDayRepository.save(new MatchDay(categoryId, season, matchDayNumber, 123L, 12345L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
//            try (Statement statement = connection.createStatement()) {
//                String insertMatchDayQuery = "INSERT INTO match_day (category_id, season, match_day_number, start_date, end_date) " +
//                        "VALUES ('" + categoryId + "', " + season + ", " + matchDayNumber + ", 123, 12345);";
//                statement.executeUpdate(insertMatchDayQuery);
//            }
//        } catch (Exception e) {
//            fail();
//        }
    }

    protected void givenSomePreviouslyStoredLeagueWithId(String categoryId, String user) {
        givenSomePreviouslyStoredLeagueCategoryWithId(categoryId);
        try {
            leagueId = leagueRepository.save(new League("Some League " + categoryId,
                    "Some description", categoryId, true, null,
                    LeagueStatus.ACTIVE, 100, null, false, user,
                    12345L, null)).getLeagueId();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
//            String insertLeaguePlayerQuery = "INSERT INTO league (name, description, category_id, is_public, " +
//                    "password, status, max_players, logo_url, is_premium, owner_username, creation_date, modification_date) " +
//                    "VALUES ('Some League " + categoryId + "', 'Some description', '" + categoryId + "', true, " +
//                    "NULL, 'ACTIVE', 100, NULL, false, '" + user + "', 12345, NULL) RETURNING league_id;";
//
//            try (Statement statement = connection.createStatement();
//                 ResultSet rs = statement.executeQuery(insertLeaguePlayerQuery)) {
//                if (rs.next()) {
//                    leagueId = rs.getLong("league_id");
//                }
//            }
//        } catch (Exception e) {
//            fail();
//        }
    }

    protected void givenSomePreviouslyStoredLeaguePlayerWithId(String user) {
        givenSomePreviouslyStoredLeagueWithId("someCategory", user);
        givenSomePreviouslyStoredLeagueCategoryWithId("someCategory");

        try {
            leaguePlayerRepository.save(new LeaguePlayer(leagueId, user, 55, PlayerStatus.ACTIVE, 12345L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
//            try (Statement statement = connection.createStatement()) {
//                String insertLeaguePlayerQuery = "INSERT INTO league_player (league_id, username, total_points, player_status, join_date) " +
//                        "VALUES (" + leagueId + ", '" + user + "', 55, 'ACTIVE', 12345) ON CONFLICT DO NOTHING;";
//                statement.executeUpdate(insertLeaguePlayerQuery);
//            }
//        } catch (Exception e) {
//            fail();
//        }
    }
}

