
package com.pmolinav.predictions.controllers;

import com.pmolinav.predictions.exceptions.InternalServerErrorException;
import com.pmolinav.predictions.exceptions.NotFoundException;
import com.pmolinav.predictions.services.PlayerBetService;
import com.pmolinav.predictionslib.dto.PlayerBetDTO;
import com.pmolinav.predictionslib.model.PlayerBet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("player-bets")
public class PlayerBetController {

    private final PlayerBetService playerBetService;

    @Autowired
    public PlayerBetController(PlayerBetService playerBetService) {
        this.playerBetService = playerBetService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerBetDTO>> findAll() {
        try {
            List<PlayerBetDTO> bets = playerBetService.findAll();
            return ResponseEntity.ok(bets);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<PlayerBetDTO> findById(@PathVariable Long id) {
        try {
            PlayerBetDTO bet = playerBetService.findById(id);
            return ResponseEntity.ok(bet);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<PlayerBetDTO>> findByMatchId(@PathVariable Long matchId) {
        try {
            List<PlayerBetDTO> bets = playerBetService.findByMatchId(matchId);
            return ResponseEntity.ok(bets);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<PlayerBetDTO>> findByUsername(@PathVariable String username) {
        try {
            List<PlayerBetDTO> bets = playerBetService.findByUsername(username);
            return ResponseEntity.ok(bets);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PlayerBetDTO dto) {
        try {
            if (dto.getTotalStake() == null) {
                if (dto.getSelections() != null && !dto.getSelections().isEmpty()) {
                    dto.setCalculatedStake();
                } else {
                    return new ResponseEntity<>("Selections cannot be null or empty", HttpStatus.BAD_REQUEST);
                }
            }
            PlayerBet created = playerBetService.create(dto);
            return new ResponseEntity<>(created.getBetId(), HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            playerBetService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/match/{matchId}")
    public ResponseEntity<Void> deleteByMatchId(@PathVariable Long matchId) {
        try {
            playerBetService.deleteByMatchId(matchId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        try {
            playerBetService.deleteByUsername(username);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
