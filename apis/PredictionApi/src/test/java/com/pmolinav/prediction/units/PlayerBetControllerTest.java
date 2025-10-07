package com.pmolinav.prediction.units;

import com.pmolinav.prediction.exceptions.CustomStatusException;
import com.pmolinav.prediction.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.MatchStatus;
import com.pmolinav.predictionslib.dto.PlayerBetByUsernameDTO;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import com.pmolinav.predictionslib.dto.SimpleMatchDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PlayerBetControllerTest extends BaseUnitTest {

    private PlayerBetDTO playerBetDTO;
    private List<PlayerBetDTO> expectedPlayerBets;
    private List<PlayerBetByUsernameDTO> expectedPlayerBetsByUsername;
    private ResponseEntity<?> result;

    /* FIND BY ID */
    @Test
    void findByIdHappyPath() {
        whenFindPlayerBetByIdReturnsValidDTO();
        andFindPlayerBetByIdIsCalled();
        thenVerifyFindPlayerBetByIdCalled();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseIs(playerBetDTO);
    }

    @Test
    void findByIdNotFound() {
        whenFindPlayerBetByIdThrowsNotFound();
        andFindPlayerBetByIdIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findByIdServerError() {
        whenFindPlayerBetByIdThrowsServerError();
        andFindPlayerBetByIdIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND BY MATCH ID */
    @Test
    void findByMatchIdHappyPath() {
        whenFindPlayersBetsByMatchIdReturnsValidDTO();
        andFindPlayerBetsByMatchIdIsCalled();
        thenVerifyFindPlayerBetsByMatchIdCalled();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListIs(expectedPlayerBets);
    }

    @Test
    void findByMatchIdNotFound() {
        whenFindPlayerBetsByMatchIdThrowsNotFound();
        andFindPlayerBetsByMatchIdIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findByMatchIdServerError() {
        whenFindPlayerBetsByMatchIdThrowsServerError();
        andFindPlayerBetsByMatchIdIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND BY USERNAME */
    @Test
    void findByUsernameHappyPath() {
        whenFindPlayersBetsByUsernameReturnsValidDTO();
        andFindPlayerBetsByUsernameIsCalled();
        thenVerifyFindPlayerBetsByUsernameCalled();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListByUsernameIs(expectedPlayerBetsByUsername);
    }

    @Test
    void findByUsernameNotFound() {
        whenFindPlayerBetsByUsernameThrowsNotFound();
        andFindPlayerBetsByUsernameIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findByUsernameServerError() {
        whenFindPlayerBetsByUsernameThrowsServerError();
        andFindPlayerBetsByUsernameIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CREATE */
    @Test
    void createHappyPath() {
        givenValidPlayerBetDTO();
        whenCreatePlayerBetReturnsId();
        andCreatePlayerBetIsCalled();
        thenVerifyCreatePlayerBetCalled();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseLongIs(1L);
    }

    @Test
    void createBadRequest() {
        givenValidPlayerBetDTO();
        andCreatePlayerBetCalledWithErrors();
        thenReceivedStatusCodeIs(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createServerError() {
        givenValidPlayerBetDTO();
        whenCreatePlayerBetThrowsServerError();
        andCreatePlayerBetIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE */
    @Test
    void deleteHappyPath() {
        whenDeletePlayerBetDoesNothing();
        andDeletePlayerBetIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteNotFound() {
        whenDeletePlayerBetThrowsNotFound();
        andDeletePlayerBetIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteServerError() {
        whenDeletePlayerBetThrowsServerError();
        andDeletePlayerBetIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- SETUP MOCKS ---

    private void givenValidPlayerBetDTO() {
        playerBetDTO = new PlayerBetDTO("someUser", 2L, 1L, BigDecimal.TEN, null);
    }

    private void whenFindPlayerBetByIdReturnsValidDTO() {
        playerBetDTO = new PlayerBetDTO("someUser", 2L, 1L, BigDecimal.TEN, null);
        when(playerBetServiceMock.findById(1L)).thenReturn(playerBetDTO);
    }

    private void whenFindPlayerBetByIdThrowsNotFound() {
        when(playerBetServiceMock.findById(1L)).thenThrow(new NotFoundException("Not found"));
    }

    private void whenFindPlayerBetByIdThrowsServerError() {
        when(playerBetServiceMock.findById(1L)).thenThrow(new CustomStatusException("Error", 500));
    }

    private void whenFindPlayersBetsByMatchIdReturnsValidDTO() {
        expectedPlayerBets = List.of(
                new PlayerBetDTO("someUser", 2L, 1L, BigDecimal.TEN, null)
        );
        when(playerBetServiceMock.findByMatchId(2L)).thenReturn(expectedPlayerBets);
    }

    private void whenFindPlayerBetsByMatchIdThrowsNotFound() {
        when(playerBetServiceMock.findByMatchId(2L)).thenThrow(new NotFoundException("Not found"));
    }

    private void whenFindPlayerBetsByMatchIdThrowsServerError() {
        when(playerBetServiceMock.findByMatchId(2L)).thenThrow(new CustomStatusException("Error", 500));
    }

    private void whenFindPlayersBetsByUsernameReturnsValidDTO() {
        expectedPlayerBetsByUsername = List.of(
                new PlayerBetByUsernameDTO("someUser",
                        new SimpleMatchDTO("Home", "Away", 1234L, MatchStatus.ACTIVE),
                        1L, BigDecimal.TEN, null)
        );
        when(playerBetServiceMock.findByUsername("someUser")).thenReturn(expectedPlayerBetsByUsername);
    }

    private void whenFindPlayerBetsByUsernameThrowsNotFound() {
        when(playerBetServiceMock.findByUsername("someUser")).thenThrow(new NotFoundException("Not found"));
    }

    private void whenFindPlayerBetsByUsernameThrowsServerError() {
        when(playerBetServiceMock.findByUsername("someUser")).thenThrow(new CustomStatusException("Error", 500));
    }

    private void whenCreatePlayerBetReturnsId() {
        when(playerBetServiceMock.create(any())).thenReturn(1L);
    }

    private void whenCreatePlayerBetThrowsServerError() {
        when(playerBetServiceMock.create(any())).thenThrow(new CustomStatusException("Error", 500));
    }

    private void whenDeletePlayerBetDoesNothing() {
        doNothing().when(playerBetServiceMock).delete(1L);
    }

    private void whenDeletePlayerBetThrowsNotFound() {
        doThrow(new NotFoundException("Not found")).when(playerBetServiceMock).delete(1L);
    }

    private void whenDeletePlayerBetThrowsServerError() {
        doThrow(new CustomStatusException("Error", 500)).when(playerBetServiceMock).delete(1L);
    }

    // --- CALL CONTROLLER ---

    private void andFindPlayerBetByIdIsCalled() {
        result = playerBetController.findPlayerBetById(requestUid, 1L);
    }

    private void andFindPlayerBetsByMatchIdIsCalled() {
        result = playerBetController.findPlayerBetsByMatchId(requestUid, 2L);
    }

    private void andFindPlayerBetsByUsernameIsCalled() {
        result = playerBetController.findPlayerBetsByUsername(requestUid, "someUser");
    }

    private void andCreatePlayerBetIsCalled() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = playerBetController.createPlayerBet(requestUid, playerBetDTO, bindingResult);
    }

    private void andCreatePlayerBetCalledWithErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("playerBetDTO", "playerId", "Player ID is required")
        ));
        result = playerBetController.createPlayerBet(requestUid, playerBetDTO, bindingResult);
    }

    private void andDeletePlayerBetIsCalled() {
        result = playerBetController.deletePlayerBet(requestUid, 1L);
    }

    // --- VERIFY ---

    private void thenVerifyFindPlayerBetByIdCalled() {
        verify(playerBetServiceMock, times(1)).findById(1L);
    }

    private void thenVerifyFindPlayerBetsByMatchIdCalled() {
        verify(playerBetServiceMock, times(1)).findByMatchId(2L);
    }

    private void thenVerifyFindPlayerBetsByUsernameCalled() {
        verify(playerBetServiceMock, times(1)).findByUsername("someUser");
    }

    private void thenVerifyCreatePlayerBetCalled() {
        verify(playerBetServiceMock, times(1)).create(any());
    }

    // --- ASSERTIONS ---

    private void thenReceivedStatusCodeIs(HttpStatus status) {
        assertEquals(status, result.getStatusCode());
    }

    private void thenReceivedResponseIs(PlayerBetDTO expected) {
        assertNotNull(result);
        assertEquals(expected, result.getBody());
    }

    private void thenReceivedResponseListIs(List<PlayerBetDTO> expected) {
        assertNotNull(result);
        assertEquals(expected, result.getBody());
    }

    private void thenReceivedResponseListByUsernameIs(List<PlayerBetByUsernameDTO> expected) {
        assertNotNull(result);
        assertEquals(expected, result.getBody());
    }

    private void thenReceivedResponseLongIs(Long expected) {
        assertNotNull(result);
        assertEquals(expected, result.getBody());
    }
}