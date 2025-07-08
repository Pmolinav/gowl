package com.pmolinav.predictions.units;

import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.EventType;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
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

class PlayerBetSelectionBOControllerTest extends BaseUnitTest {

    private PlayerBetSelectionDTO dto;
    private List<PlayerBetSelectionDTO> expectedList;
    private ResponseEntity<?> result;

    /* FIND ALL */
    @Test
    void findAllHappyPath() {
        whenFindAllReturnsList();
        andFindAllIsCalled();
        thenVerifyFindAllCalled();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseListIs(expectedList);
    }

    @Test
    void findAllNotFound() {
        whenFindAllThrowsNotFound();
        andFindAllIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllServerError() {
        whenFindAllThrowsServerError();
        andFindAllIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* FIND BY ID */
    @Test
    void findByIdHappyPath() {
        whenFindByIdReturnsDTO();
        andFindByIdIsCalled();
        thenVerifyFindByIdCalled();
        thenReceivedStatusCodeIs(HttpStatus.OK);
        thenReceivedResponseIs(dto);
    }

    @Test
    void findByIdNotFound() {
        whenFindByIdThrowsNotFound();
        andFindByIdIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void findByIdServerError() {
        whenFindByIdThrowsServerError();
        andFindByIdIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* CREATE */
    @Test
    void createHappyPath() {
        givenValidDTO();
        whenCreateReturnsId();
        andCreateIsCalled();
        thenVerifyCreateCalled();
        thenReceivedStatusCodeIs(HttpStatus.CREATED);
        thenReceivedResponseLongIs(1L);
    }

    @Test
    void createBadRequest() {
        givenValidDTO();
        andCreateCalledWithErrors();
        thenReceivedStatusCodeIs(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createServerError() {
        givenValidDTO();
        whenCreateThrowsServerError();
        andCreateIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* DELETE */
    @Test
    void deleteHappyPath() {
        whenDeleteDoesNothing();
        andDeleteIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.OK);
    }

    @Test
    void deleteNotFound() {
        whenDeleteThrowsNotFound();
        andDeleteIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.NOT_FOUND);
    }

    @Test
    void deleteServerError() {
        whenDeleteThrowsServerError();
        andDeleteIsCalled();
        thenReceivedStatusCodeIs(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- SETUP MOCKS ---

    private void givenValidDTO() {
        dto = new PlayerBetSelectionDTO(EventType.H2H.getName(), 3L, BigDecimal.ONE);
    }

    private void whenFindAllReturnsList() {
        expectedList = List.of(
                new PlayerBetSelectionDTO(EventType.H2H.getName(), 3L, BigDecimal.ONE),
                new PlayerBetSelectionDTO(EventType.H2H.getName(), 4L, BigDecimal.ONE)
        );
        when(playerBetSelectionBOServiceMock.findAll()).thenReturn(expectedList);
    }

    private void whenFindAllThrowsNotFound() {
        when(playerBetSelectionBOServiceMock.findAll()).thenThrow(new NotFoundException("Not found"));
    }

    private void whenFindAllThrowsServerError() {
        when(playerBetSelectionBOServiceMock.findAll()).thenThrow(new CustomStatusException("Error", 500));
    }

    private void whenFindByIdReturnsDTO() {
        dto = new PlayerBetSelectionDTO(EventType.H2H.getName(), 3L, BigDecimal.ONE);
        when(playerBetSelectionBOServiceMock.findById(1L)).thenReturn(dto);
    }

    private void whenFindByIdThrowsNotFound() {
        when(playerBetSelectionBOServiceMock.findById(1L)).thenThrow(new NotFoundException("Not found"));
    }

    private void whenFindByIdThrowsServerError() {
        when(playerBetSelectionBOServiceMock.findById(1L)).thenThrow(new CustomStatusException("Error", 500));
    }

    private void whenCreateReturnsId() {
        when(playerBetSelectionBOServiceMock.create(any())).thenReturn(1L);
    }

    private void whenCreateThrowsServerError() {
        when(playerBetSelectionBOServiceMock.create(any())).thenThrow(new CustomStatusException("Error", 500));
    }

    private void whenDeleteDoesNothing() {
        doNothing().when(playerBetSelectionBOServiceMock).delete(1L);
    }

    private void whenDeleteThrowsNotFound() {
        doThrow(new NotFoundException("Not found")).when(playerBetSelectionBOServiceMock).delete(1L);
    }

    private void whenDeleteThrowsServerError() {
        doThrow(new CustomStatusException("Error", 500)).when(playerBetSelectionBOServiceMock).delete(1L);
    }

    // --- CALL CONTROLLER ---

    private void andFindAllIsCalled() {
        result = playerBetSelectionBOController.findAllSelections(requestUid);
    }

    private void andFindByIdIsCalled() {
        result = playerBetSelectionBOController.findSelectionById(requestUid, 1L);
    }

    private void andCreateIsCalled() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        result = playerBetSelectionBOController.createSelection(requestUid, dto, bindingResult);
    }

    private void andCreateCalledWithErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("dto", "oddsId", "Odds is required")
        ));
        result = playerBetSelectionBOController.createSelection(requestUid, dto, bindingResult);
    }

    private void andDeleteIsCalled() {
        result = playerBetSelectionBOController.deleteSelection(requestUid, 1L);
    }

    // --- VERIFY ---

    private void thenVerifyFindAllCalled() {
        verify(playerBetSelectionBOServiceMock, times(1)).findAll();
    }

    private void thenVerifyFindByIdCalled() {
        verify(playerBetSelectionBOServiceMock, times(1)).findById(1L);
    }

    private void thenVerifyCreateCalled() {
        verify(playerBetSelectionBOServiceMock, times(1)).create(any());
    }

    // --- ASSERTIONS ---

    private void thenReceivedStatusCodeIs(HttpStatus status) {
        assertEquals(status, result.getStatusCode());
    }

    private void thenReceivedResponseIs(PlayerBetSelectionDTO expected) {
        assertNotNull(result);
        assertEquals(expected, result.getBody());
    }

    private void thenReceivedResponseListIs(List<PlayerBetSelectionDTO> expected) {
        assertNotNull(result);
        assertEquals(expected, result.getBody());
    }

    private void thenReceivedResponseLongIs(Long expected) {
        assertNotNull(result);
        assertEquals(expected, result.getBody());
    }
}
