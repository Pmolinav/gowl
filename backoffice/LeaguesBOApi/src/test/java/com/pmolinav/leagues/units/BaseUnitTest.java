package com.pmolinav.leagues.units;

import com.pmolinav.leagues.controllers.LeagueBOController;
import com.pmolinav.leagues.controllers.LeagueCategoryBOController;
import com.pmolinav.leagues.services.LeagueCategoriesBOService;
import com.pmolinav.leagues.services.LeaguesBOService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;

@RunWith(MockitoJUnitRunner.class)
class BaseUnitTest {

    @Mock
    LeaguesBOService leaguesBOServiceMock;
    @InjectMocks
    LeagueBOController leagueBOController;

    @Mock
    LeagueCategoriesBOService leagueCategoriesBOServiceMock;
    @InjectMocks
    LeagueCategoryBOController leagueCategoriesBOController;

    @Mock
    AuthenticationManager authenticationManager;

    public final String requestUid = "someRequestUid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}
