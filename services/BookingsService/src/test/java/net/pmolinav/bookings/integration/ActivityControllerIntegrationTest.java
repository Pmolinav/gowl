package com.pmolinav.bookings.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.bookings.producer.MessageProducer;
import com.pmolinav.bookings.repository.ActivityRepository;
import com.pmolinav.userslib.dto.ActivityDTO;
import com.pmolinav.userslib.model.Activity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@EnableJpaRepositories("com.pmolinav.bookings.repository")
@EntityScan("com.pmolinav.userslib.model")
class ActivityControllerIntegrationTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void findAllActivitiesNotFound() throws Exception {
        mockMvc.perform(get("/activities"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllActivitiesHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithIds(1, 2, true, false, false);

        MvcResult result = mockMvc.perform(get("/activities"))
                .andExpect(status().isOk())
                .andReturn();

        List<Activity> activityResponseList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<Activity>>() {
                });

        Assertions.assertEquals(2, activityResponseList.size());
        Assertions.assertEquals("FOOTBALL", activityResponseList.get(0).getActivityName());
        Assertions.assertEquals("TENNIS", activityResponseList.get(1).getActivityName());
    }

    @Test
    void createActivityHappyPath() throws Exception {
        ActivityDTO requestDto = new ActivityDTO("GYM", "Gym activity", 25);

        MvcResult result = mockMvc.perform(post("/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseBody, matchesPattern("\\w+"));
    }

    @Test
    void findActivityByNameNotFound() throws Exception {
        mockMvc.perform(get("/activities/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findActivityByNameHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithIds(3, 4, true, false, false);

        MvcResult result = mockMvc.perform(get("/activities/FOOTBALL"))
                .andExpect(status().isOk())
                .andReturn();

        Activity activityResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<Activity>() {
                });

        Assertions.assertEquals("FOOTBALL", activityResponse.getActivityName());
        Assertions.assertEquals(25, activityResponse.getPrice());
    }

    @Test
    void deleteActivityByIdNotFound() throws Exception {
        mockMvc.perform(delete("/activities/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteActivityByIdHappyPath() throws Exception {
        givenSomePreviouslyStoredDataWithIds(5, 6, true, false, false);

        mockMvc.perform(delete("/activities/FOOTBALL"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/activities/TENNIS"))
                .andExpect(status().isOk());
    }
}

