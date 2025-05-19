package com.pmolinav.leagues.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.leagues.repositories.*;
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
    protected LeaguePlayerPointsRepository leaguePlayerPointsRepository;
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
            leaguePlayerPointsRepository.deleteAll();
            matchDayRepository.deleteAll();
            leaguePlayerRepository.deleteAll();
            leagueRepository.deleteAll();
            leagueCategoryRepository.deleteAll();
        } catch (Exception e) {
            fail();
        }
    }

    protected void givenSomePreviouslyStoredLeagueCategoryWithId(String categoryId) {
        try {
            leagueCategoryRepository.save(new LeagueCategory(categoryId, "Some League Category",
                    "Some Description", "FOOTBALL", "ES", null,
                    true, 1L, 1L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected void givenSomePreviouslyStoredMatchDayWithId(String categoryId, Integer season, Integer matchDayNumber) {
        givenSomePreviouslyStoredLeagueCategoryWithId(categoryId);
        try {
            matchDayRepository.save(new MatchDay(categoryId, season, matchDayNumber, 123L, 12345L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
    }

    protected void givenSomePreviouslyStoredLeaguePlayerWithId(String user) {
        givenSomePreviouslyStoredLeagueWithId("someCategory", user);
        givenSomePreviouslyStoredLeagueCategoryWithId("someCategory");

        try {
            leaguePlayerRepository.save(new LeaguePlayer(leagueId, user, 0, PlayerStatus.ACTIVE, 12345L));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected void givenSomePreviouslyStoredLeaguePlayerPointsWithId(String user, Integer season, Integer number) {
        givenSomePreviouslyStoredLeaguePlayerWithId(user);
        givenSomePreviouslyStoredMatchDayWithId("someCategory", season, number);

        try {
            leaguePlayerPointsRepository.save(new LeaguePlayerPoints("someCategory",
                    season, number, leagueId, user, 22));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

