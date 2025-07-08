package com.pmolinav.predictions.unit;

import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.OddsDTO;
import com.pmolinav.predictionslib.model.Odds;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OddsControllerTest extends BaseUnitTest {

    OddsDTO oddsDTO;
    List<OddsDTO> expectedOdds;
    ResponseEntity<?> result;

    /* FIND ALL ODDS */
    @Test
    void findAllOddsHappyPath() {
        when(oddsServiceMock.findAllOdds()).thenReturn(expectedOdds());

        result = oddsController.findAllOdds();

        verify(oddsServiceMock).findAllOdds();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedOdds(), result.getBody());
    }

    @Test
    void findAllOddsNotFound() {
        when(oddsServiceMock.findAllOdds()).thenThrow(new NotFoundException("Not Found"));

        result = oddsController.findAllOdds();

        verify(oddsServiceMock).findAllOdds();
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findAllOddsServerError() {
        when(oddsServiceMock.findAllOdds()).thenThrow(new InternalServerErrorException("Internal Error"));

        result = oddsController.findAllOdds();

        verify(oddsServiceMock).findAllOdds();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* FIND ODDS BY MATCH ID */
    @Test
    void findOddsByMatchIdHappyPath() {
        List<OddsDTO> dtoList = List.of(new OddsDTO());
        when(oddsServiceMock.findByMatchId(1L)).thenReturn(dtoList);

        result = oddsController.findOddsByMatchId(1L);

        verify(oddsServiceMock).findByMatchId(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dtoList, result.getBody());
    }

    @Test
    void findOddsByMatchIdNotFound() {
        when(oddsServiceMock.findByMatchId(1L)).thenThrow(new NotFoundException("Not found"));

        result = oddsController.findOddsByMatchId(1L);

        verify(oddsServiceMock).findByMatchId(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findOddsByMatchIdServerError() {
        when(oddsServiceMock.findByMatchId(1L)).thenThrow(new InternalServerErrorException("Error"));

        result = oddsController.findOddsByMatchId(1L);

        verify(oddsServiceMock).findByMatchId(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* CREATE ODDS */
    @Test
    void createOddsHappyPath() {
        Odds odds = new Odds();
        odds.setOddsId(1L);
        when(oddsServiceMock.createOdds(any(OddsDTO.class))).thenReturn(odds);

        oddsDTO = new OddsDTO();
        result = oddsController.createOdds(oddsDTO);

        verify(oddsServiceMock).createOdds(any(OddsDTO.class));
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(1L, result.getBody());
    }

    @Test
    void createOddsServerError() {
        oddsDTO = new OddsDTO();
        when(oddsServiceMock.createOdds(any(OddsDTO.class))).thenThrow(new InternalServerErrorException("Error"));

        result = oddsController.createOdds(oddsDTO);

        verify(oddsServiceMock).createOdds(any(OddsDTO.class));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* FIND ODDS BY ID */
    @Test
    void findOddsByIdHappyPath() {
        OddsDTO dto = new OddsDTO();
        when(oddsServiceMock.findOddsById(1L)).thenReturn(dto);

        result = oddsController.findOddsById(1L);

        verify(oddsServiceMock).findOddsById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(dto, result.getBody());
    }

    @Test
    void findOddsByIdNotFound() {
        when(oddsServiceMock.findOddsById(1L)).thenThrow(new NotFoundException("Not found"));

        result = oddsController.findOddsById(1L);

        verify(oddsServiceMock).findOddsById(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void findOddsByIdServerError() {
        when(oddsServiceMock.findOddsById(1L)).thenThrow(new InternalServerErrorException("Error"));

        result = oddsController.findOddsById(1L);

        verify(oddsServiceMock).findOddsById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* DELETE ODDS BY ID */
    @Test
    void deleteOddsByIdHappyPath() {
        doNothing().when(oddsServiceMock).deleteOdds(1L);

        result = oddsController.deleteOdds(1L);

        verify(oddsServiceMock).deleteOdds(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteOddsByIdNotFound() {
        doThrow(new NotFoundException("Not found")).when(oddsServiceMock).deleteOdds(1L);

        result = oddsController.deleteOdds(1L);

        verify(oddsServiceMock).deleteOdds(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void deleteOddsByIdServerError() {
        doThrow(new InternalServerErrorException("Error")).when(oddsServiceMock).deleteOdds(1L);

        result = oddsController.deleteOdds(1L);

        verify(oddsServiceMock).deleteOdds(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* DELETE ODDS BY MATCH ID */
    @Test
    void deleteOddsByMatchIdHappyPath() {
        doNothing().when(oddsServiceMock).deleteOddsByMatchId(1L);

        result = oddsController.deleteOddsByMatchId(1L);

        verify(oddsServiceMock).deleteOddsByMatchId(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteOddsByMatchIdNotFound() {
        doThrow(new NotFoundException("Not found")).when(oddsServiceMock).deleteOddsByMatchId(1L);

        result = oddsController.deleteOddsByMatchId(1L);

        verify(oddsServiceMock).deleteOddsByMatchId(1L);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void deleteOddsByMatchIdServerError() {
        doThrow(new InternalServerErrorException("Error")).when(oddsServiceMock).deleteOddsByMatchId(1L);

        result = oddsController.deleteOddsByMatchId(1L);

        verify(oddsServiceMock).deleteOddsByMatchId(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /* Helpers */
    private List<OddsDTO> expectedOdds() {
        OddsDTO o1 = new OddsDTO();
        o1.setActive(true);
        o1.setLabel("Over 2.5 Goals");
        o1.setValue(BigDecimal.valueOf(2.5));

        OddsDTO o2 = new OddsDTO();
        o2.setActive(true);
        o1.setLabel("Winner");
        o2.setValue(BigDecimal.valueOf(3.1));

        return List.of(o1, o2);
    }

}
