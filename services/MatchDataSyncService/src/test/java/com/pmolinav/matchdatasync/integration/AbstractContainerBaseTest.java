package com.pmolinav.matchdatasync.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.matchdatasync.repositories.*;
import com.pmolinav.predictionslib.dto.EventType;
import com.pmolinav.predictionslib.dto.MatchStatus;
import com.pmolinav.predictionslib.model.Event;
import com.pmolinav.predictionslib.model.Match;
import com.pmolinav.predictionslib.model.Odds;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    protected static Match lastMatch;

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
    protected ExternalCategoryMappingRepository mappingRepository;
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
            playerBetRepository.deleteAll();
            oddsRepository.deleteAll();
            eventRepository.deleteAll();
            matchRepository.deleteAll();
            mappingRepository.deleteAll();
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
        match.setStatus(MatchStatus.ACTIVE);

        lastMatch = matchRepository.save(match);
        return lastMatch;
    }

    protected Event givenSomePreviouslyStoredEventWithId() {
        Match match = givenSomePreviouslyStoredMatchWithId();

        Event event = new Event();
        event.setEventType(EventType.H2H.getName());
        event.setDescription("Number of goals in the match");

        return eventRepository.save(event);
    }

    protected Event givenSomePreviouslyStoredEventWithMatchId(long matchId) {
        Event event = new Event();
        event.setEventType(EventType.H2H.getName());
        event.setDescription("Number of goals in the match");

        return eventRepository.save(event);
    }

    protected Odds givenSomePreviouslyStoredOddsWithId() {
        Event event = givenSomePreviouslyStoredEventWithId();

        Odds odds = new Odds();
        odds.setEventType(event.getEventType());
        odds.setLabel("Over 2.5");
        odds.setValue(BigDecimal.valueOf(2.10));
        odds.setActive(true);

        return oddsRepository.save(odds);
    }

    protected String readJsonFromResource(String relativePath) {
        try {
            // From the current test directory: src/test/java (as default).
            Path basePath = Paths.get("src/test/java")
                    .resolve(Paths.get(this.getClass()
                            .getPackageName().replace(".", "/"))); // Folder where the test is.

            Path fullPath = basePath.resolve(relativePath);
            return Files.readString(fullPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read JSON from relative file: " + relativePath, e);
        }
    }
}

