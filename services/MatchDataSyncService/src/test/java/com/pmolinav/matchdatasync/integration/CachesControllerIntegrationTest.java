package com.pmolinav.matchdatasync.integration;

import com.pmolinav.predictionslib.model.ExternalCategoryMapping;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void testCacheLifecycleCompleteControllerTest() throws Exception {
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

    private void givenCategoryMappingPreviouslyStored() {
        ExternalCategoryMapping mapping = new ExternalCategoryMapping(
                this.categoryId,
                "soccer_spain_la_liga"
        );
        mappingRepository.save(mapping);
    }
}

