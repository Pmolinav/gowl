package com.pmolinav.matchdatasync.integration;

import com.pmolinav.predictionslib.dto.EventType;
import com.pmolinav.predictionslib.dto.MatchStatus;
import com.pmolinav.predictionslib.model.Event;
import com.pmolinav.predictionslib.model.ExternalCategoryMapping;
import com.pmolinav.predictionslib.model.Match;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EntityScan("com.pmolinav.predictionslib.model")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CachesControllerIntegrationTest extends AbstractContainerBaseTest {

    private final String categoryId = "LA_LIGA";

    @Test
    void testCategoryCacheLifecycleCompleteControllerTest() throws Exception {
        // 1. Store mapping directly in the database.
        givenCategoryMappingPreviouslyStored();

        // 2. GET with `onlyCache=true` → Not found (not present in cache yet).
        mockMvc.perform(get("/caches/categories/" + categoryId)
                        .param("onlyCache", "true"))
                .andExpect(status().isNotFound());

        // 3. GET without onlyCache → Found, and the value is cached.
        mockMvc.perform(get("/caches/categories/" + categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(categoryId))
                .andExpect(jsonPath("$.externalSportKey").value("soccer_spain_la_liga"));

        // 4. GET from cache again → Value is found.
        mockMvc.perform(get("/caches/categories/" + categoryId)
                        .param("onlyCache", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(categoryId));

        // 5. DELETE Invalidate cache by categoryId.
        mockMvc.perform(delete("/caches/categories")
                        .param("categoryId", categoryId))
                .andExpect(status().isOk());

        // 6. GET again from cache → Not found. It was removed.
        mockMvc.perform(get("/caches/categories/" + categoryId)
                        .param("onlyCache", "true"))
                .andExpect(status().isNotFound());

        // 7. GET from cache or repository → Found (from repository) and value is cached again.
        mockMvc.perform(get("/caches/categories/" + categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(categoryId));

        // 8. DELETE to test endpoint to invalidate all.
        mockMvc.perform(delete("/caches/categories"))
                .andExpect(status().isOk());

        // 9. GET only from cache again → Not present, due to all data were removed.
        mockMvc.perform(get("/caches/categories/" + categoryId)
                        .param("onlyCache", "true"))
                .andExpect(status().isNotFound());

        // 10. GET from all sources again → Found in repo and cached again.
        mockMvc.perform(get("/caches/categories/" + categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(categoryId));
    }

    @Test
    void testEventCacheLifecycleCompleteControllerTest() throws Exception {
        // 1. Store event directly in the database.
        String type = givenEventPreviouslyStored();

        // 2. GET with `onlyCache=true` → Not found (not present in cache yet).
        mockMvc.perform(get("/caches/events/" + type)
                        .param("onlyCache", "true"))
                .andExpect(status().isNotFound());

        // 3. GET without onlyCache → Found, and the value is cached.
        mockMvc.perform(get("/caches/events/" + type))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventType").value(type));

        // 4. GET from cache again → Value is found.
        mockMvc.perform(get("/caches/events/" + type)
                        .param("onlyCache", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventType").value(type));

        // 5. DELETE Invalidate cache by eventType.
        mockMvc.perform(delete("/caches/events")
                        .param("eventType", String.valueOf(type)))
                .andExpect(status().isOk());

        // 6. GET again from cache → Not found. It was removed.
        mockMvc.perform(get("/caches/events/" + type)
                        .param("onlyCache", "true"))
                .andExpect(status().isNotFound());

        // 7. GET from cache or repository → Found (from repository) and value is cached again.
        mockMvc.perform(get("/caches/events/" + type))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventType").value(type));

        // 8. DELETE to test endpoint to invalidate all.
        mockMvc.perform(delete("/caches/events"))
                .andExpect(status().isOk());

        // 9. GET only from cache again → Not present, due to all data were removed.
        mockMvc.perform(get("/caches/events/" + type)
                        .param("onlyCache", "true"))
                .andExpect(status().isNotFound());

        // 10. GET from all sources again → Found in repo and cached again.
        mockMvc.perform(get("/caches/events/" + type))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventType").value(type));
    }

    private String givenEventPreviouslyStored() {
        Match match = new Match(null, "LA_LIGA", 2025, 1,
                "Valencia", "Espanyol", 1234567L, MatchStatus.SCHEDULED,
                null, null);

        Match savedMatch = matchRepository.save(match);

        Event event = new Event(EventType.H2H.getName(), savedMatch.getMatchId(),
                EventType.H2H.getDescription(), 1234567L, 1234567L);

        Event saved = eventRepository.save(event);
        return saved.getEventType();
    }

    private void givenCategoryMappingPreviouslyStored() {
        ExternalCategoryMapping mapping = new ExternalCategoryMapping(
                this.categoryId,
                "soccer_spain_la_liga"
        );
        mappingRepository.save(mapping);
    }
}

