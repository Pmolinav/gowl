package com.pmolinav.predictions.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.predictions.repositories.*;
import com.pmolinav.predictionslib.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.fail;

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

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected MatchRepository matchRepository;
    @Autowired
    protected OddsRepository oddsRepository;
    @Autowired
    protected EventRepository eventRepository;
    @Autowired
    protected PlayerBetRepository playerBetRepository;
    @Autowired
    protected PlayerBetSelectionRepository playerBetSelectionRepository;
    @Autowired
    protected final ObjectMapper objectMapper = new ObjectMapper();

    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.0")
            .withDatabaseName("predictions")
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
            playerBetSelectionRepository.deleteAll();
            oddsRepository.deleteAll();
            playerBetRepository.deleteAll();
            eventRepository.deleteAll();
            matchRepository.deleteAll();
        } catch (Exception e) {
            fail();
        }
    }

    protected Match givenSomePreviouslyStoredMatchWithId() {
        Match match = new Match();
        match.setCategoryId("PREMIER");
        match.setMatchDayNumber(1);
        match.setSeason(2024);
        match.setHomeTeam("Team A");
        match.setAwayTeam("Team B");
        match.setStartTime(System.currentTimeMillis());
        match.setStatus("ACTIVE");

        return matchRepository.save(match);
    }

    protected Event givenSomePreviouslyStoredEventWithId() {
        Match match = givenSomePreviouslyStoredMatchWithId();

        Event event = new Event();
        event.setMatchId(match.getMatchId());
        event.setName("Goals");
        event.setDescription("Number of goals in the match");

        return eventRepository.save(event);
    }

    protected Event givenSomePreviouslyStoredEventWithMatchId(long matchId) {
        Event event = new Event();
        event.setMatchId(matchId);
        event.setName("Goals");
        event.setDescription("Number of goals in the match");

        return eventRepository.save(event);
    }

    protected Odds givenSomePreviouslyStoredOddsWithId() {
        Event event = givenSomePreviouslyStoredEventWithId();

        Odds odds = new Odds();
        odds.setEventId(event.getEventId());
        odds.setLabel("Over 2.5");
        odds.setValue(BigDecimal.valueOf(2.10));
        odds.setActive(true);

        return oddsRepository.save(odds);
    }

    protected PlayerBet givenSomePreviouslyStoredPlayerBetWithId(String username) {
        Match match = givenSomePreviouslyStoredMatchWithId();

        PlayerBet bet = new PlayerBet();
        bet.setUsername(username);
        bet.setMatchId(match.getMatchId());

        return playerBetRepository.save(bet);
    }

    protected PlayerBetSelection givenSomePreviouslyStoredPlayerBetSelectionWithId(String username) {
        Odds odds = givenSomePreviouslyStoredOddsWithId();
        PlayerBet bet = givenSomePreviouslyStoredPlayerBetWithId(username);

        PlayerBetSelection selection = new PlayerBetSelection();
        selection.setBetId(bet.getBetId());
        selection.setOddsId(odds.getOddsId());
        selection.setStake(BigDecimal.valueOf(50.00));

        return playerBetSelectionRepository.save(selection);
    }
}

