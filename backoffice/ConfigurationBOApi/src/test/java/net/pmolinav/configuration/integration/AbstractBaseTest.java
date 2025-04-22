package net.pmolinav.configuration.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.pmolinav.bookingslib.dto.Role;
import net.pmolinav.bookingslib.model.User;
import net.pmolinav.configuration.client.UserClient;
import net.pmolinav.configuration.security.AuthCredentials;
import net.pmolinav.configuration.security.WebSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

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
    @MockBean
    protected UserClient userClient;
    @Autowired
    protected final ObjectMapper objectMapper = new ObjectMapper();
    private AuthCredentials request;
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
        this.request = new AuthCredentials(username, password);
    }

    protected void giveSomeUserFromClientOK() {
        User returnedUser = new User(1L,
                this.request.getUsername(),
                WebSecurityConfig.passwordEncoder().encode(this.request.getPassword()),
                "somename",
                "soem@email.com",
                Role.USER.name(),
                new Date(),
                null);
        when(userClient.findUserByUsername(anyString())).thenReturn(returnedUser);
        //Mock with stubFor
//        stubFor(get(urlEqualTo("/users/brauls/repos"))
//                .willReturn(aResponse()
//                        .withHeader("Content-Type", "application/vnd.github.v3+json")
//                        .withBody(jsonBody)));
    }
}

