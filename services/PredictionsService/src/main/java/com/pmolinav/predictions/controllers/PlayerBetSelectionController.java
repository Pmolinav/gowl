
package com.pmolinav.predictions.controllers;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.services.PlayerBetSelectionService;
import com.pmolinav.predictionslib.dto.PlayerBetSelectionDTO;
import com.pmolinav.predictionslib.model.PlayerBetSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("player-bet-selections")
public class PlayerBetSelectionController {

    @Autowired
    private final PlayerBetSelectionService playerBetSelectionService;

    @Autowired
    public PlayerBetSelectionController(PlayerBetSelectionService playerBetSelectionService) {
        this.playerBetSelectionService = playerBetSelectionService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerBetSelectionDTO>> findAll() {
        try {
            return ResponseEntity.ok(playerBetSelectionService.findAll());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<PlayerBetSelectionDTO> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(playerBetSelectionService.findById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody PlayerBetSelectionDTO dto) {
        try {
            PlayerBetSelection created = playerBetSelectionService.create(dto);
            return new ResponseEntity<>(created.getSelectionId(), HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            playerBetSelectionService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/bet/{betId}")
    public ResponseEntity<List<PlayerBetSelectionDTO>> findByBetId(@PathVariable Long betId) {
        try {
            return ResponseEntity.ok(playerBetSelectionService.findByBetId(betId));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/odd/{oddId}")
    public ResponseEntity<List<PlayerBetSelectionDTO>> findByOddId(@PathVariable Long oddId) {
        try {
            return ResponseEntity.ok(playerBetSelectionService.findByOddId(oddId));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/bet/{betId}/odd/{oddId}")
    public ResponseEntity<List<PlayerBetSelectionDTO>> findByBetIdAndOddId(@PathVariable Long betId, @PathVariable Long oddId) {
        try {
            return ResponseEntity.ok(playerBetSelectionService.findByBetIdAndOddId(betId, oddId));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/bet/{betId}")
    public ResponseEntity<Void> deleteByBetId(@PathVariable Long betId) {
        try {
            playerBetSelectionService.deleteByBetId(betId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/odd/{oddId}")
    public ResponseEntity<Void> deleteByOddId(@PathVariable Long oddId) {
        try {
            playerBetSelectionService.deleteByOddId(oddId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/bet/{betId}/odd/{oddId}")
    public ResponseEntity<Void> deleteByBetIdAndOddId(@PathVariable Long betId, @PathVariable Long oddId) {
        try {
            playerBetSelectionService.deleteByBetIdAndOddId(betId, oddId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
