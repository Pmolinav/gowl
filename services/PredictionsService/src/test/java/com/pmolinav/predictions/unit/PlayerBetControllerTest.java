package com.pmolinav.predictions.unit;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import com.pmolinav.predictionslib.model.PlayerBet;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PlayerBetControllerTest extends BaseUnitTest {

    PlayerBetDTO playerBetDTO;
    ResponseEntity<?> result;

    /* FIND ALL PLAYER BETS */
    @Test
    void findAllHappyPath() {
        List<PlayerBetDTO> list = new ArrayList<>();
        list.add(new PlayerBetDTO());
        when(playerBetServiceMock.findAll()).thenReturn(list);

        result = playerBetController.findAll();

        verify(playerBetServiceMock).findAll();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(list, result.getBody());
    }

    @Test
    void findAllNotFound() {
        when(playerBetServiceMock.findAll()).thenThrow(new NotFoundException("Not Found"));

        result = playerBetController.findAll();

        verify(playerBetServiceMock).findAll();
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findAllServerError() {
        when(playerBetServiceMock.findAll()).thenThrow(new InternalServerErrorException("Error"));

        result = playerBetController.findAll();

        verify(playerBetServiceMock).findAll();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* CREATE PLAYER BET */
    @Test
    void createPlayerBetHappyPath() {
        PlayerBet playerBet = new PlayerBet();
        playerBet.setBetId(1L);
        playerBetDTO = new PlayerBetDTO();

        when(playerBetServiceMock.create(any(PlayerBetDTO.class))).thenReturn(playerBet);

        result = playerBetController.create(playerBetDTO);

        verify(playerBetServiceMock).create(any(PlayerBetDTO.class));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(1L, result.getBody());
    }

    @Test
    void createPlayerBetServerError() {
        playerBetDTO = new PlayerBetDTO();
        when(playerBetServiceMock.create(any(PlayerBetDTO.class))).thenThrow(new InternalServerErrorException("Error"));

        result = playerBetController.create(playerBetDTO);

        verify(playerBetServiceMock).create(any(PlayerBetDTO.class));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* FIND PLAYER BET BY ID */
    @Test
    void findByIdHappyPath() {
        playerBetDTO = new PlayerBetDTO();
        when(playerBetServiceMock.findById(1L)).thenReturn(playerBetDTO);

        result = playerBetController.findById(1L);

        verify(playerBetServiceMock).findById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(playerBetDTO, result.getBody());
    }

    @Test
    void findByIdNotFound() {
        when(playerBetServiceMock.findById(1L)).thenThrow(new NotFoundException("Not found"));

        result = playerBetController.findById(1L);

        verify(playerBetServiceMock).findById(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findByIdServerError() {
        when(playerBetServiceMock.findById(1L)).thenThrow(new InternalServerErrorException("Error"));

        result = playerBetController.findById(1L);

        verify(playerBetServiceMock).findById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* DELETE PLAYER BET BY ID */
    @Test
    void deleteByIdHappyPath() {
        doNothing().when(playerBetServiceMock).deleteById(1L);

        result = playerBetController.deleteById(1L);

        verify(playerBetServiceMock).deleteById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteByIdNotFound() {
        doThrow(new NotFoundException("Not found")).when(playerBetServiceMock).deleteById(1L);

        result = playerBetController.deleteById(1L);

        verify(playerBetServiceMock).deleteById(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void deleteByIdServerError() {
        doThrow(new InternalServerErrorException("Error")).when(playerBetServiceMock).deleteById(1L);

        result = playerBetController.deleteById(1L);

        verify(playerBetServiceMock).deleteById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
}
