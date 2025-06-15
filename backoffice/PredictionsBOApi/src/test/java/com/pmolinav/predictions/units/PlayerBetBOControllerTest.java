package com.pmolinav.predictions.units;

import com.pmolinav.predictions.exceptions.CustomStatusException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PlayerBetBOControllerTest extends BaseUnitTest {

    private PlayerBetDTO playerBetDTO;
    private List<PlayerBetDTO> expectedPlayerBets;
    private ResponseEntity<?> result;

    /* FIND ALL */
    @Test
    void findAllHappyPath() {
        whenFindAllPlayerBetsInServiceReturnsValidList();
        andFindAllPlayerBetsIsCalled();
        thenVerifyFindAllPlayerBetsCalled();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListIs(expectedPlayerBets);
    }

    @Test
    void findAllNotFound() {
        whenFindAllPlayerBetsThrowsNotFound();
        andFindAllPlayerBetsIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllServerError() {
        whenFindAllPlayerBetsThrowsServerError();
        andFindAllPlayerBetsIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

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
        playerBetDTO = new PlayerBetDTO(1L, "someUser", 2L, null);
    }

    private void whenFindAllPlayerBetsInServiceReturnsValidList() {
        expectedPlayerBets = List.of(
                new PlayerBetDTO(1L, "someUser", 2L, null),
                new PlayerBetDTO(1L, "someUser", 3L, null)
        );
        when(playerBetBOServiceMock.findAll()).thenReturn(expectedPlayerBets);
    }

    private void whenFindAllPlayerBetsThrowsNotFound() {
        when(playerBetBOServiceMock.findAll()).thenThrow(new NotFoundException("No bets found"));
    }

    private void whenFindAllPlayerBetsThrowsServerError() {
        when(playerBetBOServiceMock.findAll()).thenThrow(new CustomStatusException("Error", 500));
    }

    private void whenFindPlayerBetByIdReturnsValidDTO() {
        playerBetDTO = new PlayerBetDTO(1L, "someUser", 2L, null);
        when(playerBetBOServiceMock.findById(1L)).thenReturn(playerBetDTO);
    }

    private void whenFindPlayerBetByIdThrowsNotFound() {
        when(playerBetBOServiceMock.findById(1L)).thenThrow(new NotFoundException("Not found"));
    }

    private void whenFindPlayerBetByIdThrowsServerError() {
        when(playerBetBOServiceMock.findById(1L)).thenThrow(new CustomStatusException("Error", 500));
    }

    private void whenCreatePlayerBetReturnsId() {
        when(playerBetBOServiceMock.create(any())).thenReturn(1L);
    }

    private void whenCreatePlayerBetThrowsServerError() {
        when(playerBetBOServiceMock.create(any())).thenThrow(new CustomStatusException("Error", 500));
    }

    private void whenDeletePlayerBetDoesNothing() {
        doNothing().when(playerBetBOServiceMock).delete(1L);
    }

    private void whenDeletePlayerBetThrowsNotFound() {
        doThrow(new NotFoundException("Not found")).when(playerBetBOServiceMock).delete(1L);
    }

    private void whenDeletePlayerBetThrowsServerError() {
        doThrow(new CustomStatusException("Error", 500)).when(playerBetBOServiceMock).delete(1L);
    }

    // --- CALL CONTROLLER ---

    private void andFindAllPlayerBetsIsCalled() {
        result = playerBetBOController.findAllPlayerBets(requestUid);
    }

    private void andFindPlayerBetByIdIsCalled() {
        result = playerBetBOController.findPlayerBetById(requestUid, 1L);
    }

    private void andCreatePlayerBetIsCalled() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = playerBetBOController.createPlayerBet(requestUid, playerBetDTO, bindingResult);
    }

    private void andCreatePlayerBetCalledWithErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("playerBetDTO", "playerId", "Player ID is required")
        ));
        result = playerBetBOController.createPlayerBet(requestUid, playerBetDTO, bindingResult);
    }

    private void andDeletePlayerBetIsCalled() {
        result = playerBetBOController.deletePlayerBet(requestUid, 1L);
    }

    // --- VERIFY ---

    private void thenVerifyFindAllPlayerBetsCalled() {
        verify(playerBetBOServiceMock, times(1)).findAll();
    }

    private void thenVerifyFindPlayerBetByIdCalled() {
        verify(playerBetBOServiceMock, times(1)).findById(1L);
    }

    private void thenVerifyCreatePlayerBetCalled() {
        verify(playerBetBOServiceMock, times(1)).create(any());
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

    private void thenReceivedResponseLongIs(Long expected) {
        assertNotNull(result);
        assertEquals(expected, result.getBody());
    }
}