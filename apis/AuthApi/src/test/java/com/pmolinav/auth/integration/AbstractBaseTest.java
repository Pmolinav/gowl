package com.pmolinav.auth.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.auth.auth.SpringSecurityConfig;
import com.pmolinav.auth.clients.UserClient;
import com.pmolinav.userslib.dto.UserDTO;
import com.pmolinav.userslib.model.Role;
import com.pmolinav.userslib.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
public abstract class AbstractBaseTest {
    protected static String username = "someUser";
    protected static final String password = "$2a$10$pn85ACcwW6v74Kkt3pnPau7A4lv8N2d.fvwXuLsYanv07PzlXTu9S";
    protected static final String requestUid = "someRequestUid";

    @Autowired
    protected MockMvc mockMvc;
    @MockitoBean
    protected UserClient userClient;
    @Autowired
    protected final ObjectMapper objectMapper = new ObjectMapper();
    private UserDTO request;
    protected static String authToken;

    @BeforeEach
    public void mockLoginSuccessfully() throws Exception {
        giveSomeValidRequest();
        giveSomeUserFromClientOK();

        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(this.request)))
                .andExpect(status().isOk())
                .andReturn();

        authToken = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
    }

    protected void giveSomeValidRequest() {
        this.request = new UserDTO();
        this.request.setUsername(username);
        this.request.setPassword(password);
    }

    protected void giveSomeUserFromClientOK() {
        User returnedUser = new User(1L,
                this.request.getUsername(),
                SpringSecurityConfig.passwordEncoder().encode(this.request.getPassword()),
                "somename",
                "some@email.com",
                new Date().getTime(),
                null,
                List.of(new Role(1L, com.pmolinav.auth.models.request.Role.ROLE_ADMIN.name())));

        when(userClient.findUserByUsername(anyString())).thenReturn(returnedUser);
    }
}

