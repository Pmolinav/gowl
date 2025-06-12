package com.pmolinav.predictions.unit;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.MatchDTO;
import com.pmolinav.predictionslib.model.Match;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MatchControllerTest extends BaseUnitTest {

    MatchDTO matchDTO;
    List<MatchDTO> expectedMatches;
    ResponseEntity<?> result;

    /* FIND ALL MATCHES */
    @Test
    void findAllMatchesHappyPath() {
        when(matchServiceMock.findAllMatches()).thenReturn(expectedMatches());

        result = matchController.findAllMatches();

        verify(matchServiceMock).findAllMatches();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedMatches(), result.getBody());
    }

    @Test
    void findAllMatchesNotFound() {
        when(matchServiceMock.findAllMatches()).thenThrow(new NotFoundException("Not Found"));

        result = matchController.findAllMatches();

        verify(matchServiceMock).findAllMatches();
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findAllMatchesServerError() {
        when(matchServiceMock.findAllMatches()).thenThrow(new InternalServerErrorException("Internal Error"));

        result = matchController.findAllMatches();

        verify(matchServiceMock).findAllMatches();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* CREATE MATCH */
    @Test
    void createMatchHappyPath() {
        Match match = new Match();
        match.setMatchId(1L);
        when(matchServiceMock.createMatch(any(MatchDTO.class))).thenReturn(match);

        matchDTO = new MatchDTO();
        result = matchController.createMatch(matchDTO);

        verify(matchServiceMock).createMatch(any(MatchDTO.class));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(1L, result.getBody());
    }

    @Test
    void createMatchServerError() {
        matchDTO = new MatchDTO();
        when(matchServiceMock.createMatch(any(MatchDTO.class))).thenThrow(new InternalServerErrorException("Error"));

        result = matchController.createMatch(matchDTO);

        verify(matchServiceMock).createMatch(any(MatchDTO.class));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* FIND MATCH BY ID */
    @Test
    void findByMatchIdHappyPath() {
        MatchDTO dto = new MatchDTO();
        when(matchServiceMock.findByMatchId(1L)).thenReturn(dto);

        result = matchController.findByMatchId(1L);

        verify(matchServiceMock).findByMatchId(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dto, result.getBody());
    }

    @Test
    void findByMatchIdNotFound() {
        when(matchServiceMock.findByMatchId(1L)).thenThrow(new NotFoundException("Not found"));

        result = matchController.findByMatchId(1L);

        verify(matchServiceMock).findByMatchId(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findByMatchIdServerError() {
        when(matchServiceMock.findByMatchId(1L)).thenThrow(new InternalServerErrorException("Error"));

        result = matchController.findByMatchId(1L);

        verify(matchServiceMock).findByMatchId(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* DELETE MATCH BY ID */
    @Test
    void deleteByMatchIdHappyPath() {
        doNothing().when(matchServiceMock).deleteByMatchId(1L);

        result = matchController.deleteByMatchId(1L);

        verify(matchServiceMock).deleteByMatchId(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteByMatchIdNotFound() {
        doThrow(new NotFoundException("Not found")).when(matchServiceMock).deleteByMatchId(1L);

        result = matchController.deleteByMatchId(1L);

        verify(matchServiceMock).deleteByMatchId(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void deleteByMatchIdServerError() {
        doThrow(new InternalServerErrorException("Error")).when(matchServiceMock).deleteByMatchId(1L);

        result = matchController.deleteByMatchId(1L);

        verify(matchServiceMock).deleteByMatchId(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* Helpers */
    private List<MatchDTO> expectedMatches() {
        MatchDTO m1 = new MatchDTO();
        m1.setHomeTeam("Team A");
        m1.setAwayTeam("Team B");

        MatchDTO m2 = new MatchDTO();
        m2.setHomeTeam("Team C");
        m2.setAwayTeam("Team D");

        return List.of(m1, m2);
    }

}
