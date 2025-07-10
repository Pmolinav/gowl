package com.pmolinav.predictions.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.auth.utils.TokenUtils;
import com.pmolinav.predictions.clients.PredictionsServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

@ActiveProfiles("test")
public abstract class AbstractBaseTest {
    protected static String username = "someUser";
    protected static final String password = "$2a$10$pn85ACcwW6v74Kkt3pnPau7A4lv8N2d.fvwXuLsYanv07PzlXTu9S";
    protected static final String requestUid = "someRequestUid";

    @Autowired
    protected MockMvc mockMvc;
    @MockitoBean
    protected PredictionsServiceClient predictionsServiceClient;
    @Autowired
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected static String authToken;

    @BeforeEach
    public void givenValidToken() throws Exception {
        authToken = "Bearer " + new TokenUtils("c7eD5hYnJnVr3uFTh5WTG2XKj6qbBszvuztf8WbCcJY", 12345L)
                .createToken(username, Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}

