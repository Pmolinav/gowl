//package com.pmolinav.predictions.unit;
//
//import com.pmolinav.shared.exceptions.InternalServerErrorException;
//import com.pmolinav.shared.exceptions.NotFoundException;
//import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
//import com.pmolinav.predictionslib.model.PlayerBetSelection;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class PlayerBetSelectionControllerTest extends BaseUnitTest {
//
//    PlayerBetSelectionDTO dto;
//    ResponseEntity<?> result;
//
//    /* FIND ALL */
//    @Test
//    void findAllHappyPath() {
//        List<PlayerBetSelectionDTO> list = List.of(new PlayerBetSelectionDTO(), new PlayerBetSelectionDTO());
//        when(playerBetSelectionServiceMock.findAll()).thenReturn(list);
//
//        result = playerBetSelectionController.findAll();
//
//        verify(playerBetSelectionServiceMock).findAll();
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(list, result.getBody());
//    }
//
//    @Test
//    void findAllNotFound() {
//        when(playerBetSelectionServiceMock.findAll()).thenThrow(new NotFoundException("Not found"));
//
//        result = playerBetSelectionController.findAll();
//
//        verify(playerBetSelectionServiceMock).findAll();
//        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
//    }
//
//    @Test
//    void findAllServerError() {
//        when(playerBetSelectionServiceMock.findAll()).thenThrow(new InternalServerErrorException("Error"));
//
//        result = playerBetSelectionController.findAll();
//
//        verify(playerBetSelectionServiceMock).findAll();
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
//    }
//
//    /* CREATE */
//    @Test
//    void createPlayerBetSelectionHappyPath() {
//        PlayerBetSelection entity = new PlayerBetSelection();
//        entity.setSelectionId(1L);
//
//        when(playerBetSelectionServiceMock.create(any(PlayerBetSelectionDTO.class))).thenReturn(entity);
//
//        dto = new PlayerBetSelectionDTO();
//        result = playerBetSelectionController.create(dto);
//
//        verify(playerBetSelectionServiceMock).create(any(PlayerBetSelectionDTO.class));
//        assertEquals(HttpStatus.CREATED, result.getStatusCode());
//        assertEquals(1L, result.getBody());
//    }
//
//    @Test
//    void createPlayerBetSelectionServerError() {
//        dto = new PlayerBetSelectionDTO();
//        when(playerBetSelectionServiceMock.create(any(PlayerBetSelectionDTO.class)))
//                .thenThrow(new InternalServerErrorException("Error"));
//
//        result = playerBetSelectionController.create(dto);
//
//        verify(playerBetSelectionServiceMock).create(any(PlayerBetSelectionDTO.class));
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
//    }
//
//    /* FIND BY ID */
//    @Test
//    void findPlayerBetSelectionByIdHappyPath() {
//        dto = new PlayerBetSelectionDTO();
//        when(playerBetSelectionServiceMock.findById(1L)).thenReturn(dto);
//
//        result = playerBetSelectionController.findById(1L);
//
//        verify(playerBetSelectionServiceMock).findById(1L);
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(dto, result.getBody());
//    }
//
//    @Test
//    void findPlayerBetSelectionByIdNotFound() {
//        when(playerBetSelectionServiceMock.findById(1L)).thenThrow(new NotFoundException("Not found"));
//
//        result = playerBetSelectionController.findById(1L);
//
//        verify(playerBetSelectionServiceMock).findById(1L);
//        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
//    }
//
//    @Test
//    void findPlayerBetSelectionByIdServerError() {
//        when(playerBetSelectionServiceMock.findById(1L)).thenThrow(new InternalServerErrorException("Error"));
//
//        result = playerBetSelectionController.findById(1L);
//
//        verify(playerBetSelectionServiceMock).findById(1L);
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
//    }
//
//    /* DELETE BY ID */
//    @Test
//    void deletePlayerBetSelectionByIdHappyPath() {
//        doNothing().when(playerBetSelectionServiceMock).deleteById(1L);
//
//        result = playerBetSelectionController.deleteById(1L);
//
//        verify(playerBetSelectionServiceMock).deleteById(1L);
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//    }
//
//    @Test
//    void deletePlayerBetSelectionByIdNotFound() {
//        doThrow(new NotFoundException("Not found")).when(playerBetSelectionServiceMock).deleteById(1L);
//
//        result = playerBetSelectionController.deleteById(1L);
//
//        verify(playerBetSelectionServiceMock).deleteById(1L);
//        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
//    }
//
//    @Test
//    void deletePlayerBetSelectionByIdServerError() {
//        doThrow(new InternalServerErrorException("Error")).when(playerBetSelectionServiceMock).deleteById(1L);
//
//        result = playerBetSelectionController.deleteById(1L);
//
//        verify(playerBetSelectionServiceMock).deleteById(1L);
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
//    }
//
//}
