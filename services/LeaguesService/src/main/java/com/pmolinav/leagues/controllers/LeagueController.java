
package com.pmolinav.leagues.controllers;

import com.pmolinav.leagues.exceptions.InternalServerErrorException;
import com.pmolinav.leagues.exceptions.NotFoundException;
import com.pmolinav.leagues.services.LeaguePlayerService;
import com.pmolinav.leagues.services.LeagueService;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.dto.LeaguePlayerDTO;
import com.pmolinav.leagueslib.model.League;
import com.pmolinav.leagueslib.model.PlayerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("leagues")
public class LeagueController {

    @Autowired
    private final LeagueService leagueService;
    private final LeaguePlayerService leaguePlayerService;

    @Autowired
    public LeagueController(LeagueService leagueService, LeaguePlayerService leaguePlayerService) {
        this.leagueService = leagueService;
        this.leaguePlayerService = leaguePlayerService;
    }

    //TODO: Pagination
    @GetMapping
    public ResponseEntity<List<LeagueDTO>> findAllLeagues() {
        try {
            List<LeagueDTO> leagues = leagueService.findAllLeagues();
            return ResponseEntity.ok(leagues);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<LeagueDTO> findLeagueById(@PathVariable Long id) {
        try {
            LeagueDTO league = leagueService.findById(id);
            return ResponseEntity.ok(league);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/names/{name}")
    public ResponseEntity<LeagueDTO> findLeagueByName(@PathVariable String name) {
        try {
            LeagueDTO league = leagueService.findByName(name);
            return ResponseEntity.ok(league);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<List<LeagueDTO>> findLeaguesByUsername(@PathVariable String username) {
        try {
            List<LeagueDTO> leagues = leagueService.findByUsername(username);
            return ResponseEntity.ok(leagues);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Long> createLeague(@RequestBody LeagueDTO leagueDTO) {
        try {
            League createdLeague = leagueService.createLeague(leagueDTO);

            List<LeaguePlayerDTO> leaguePlayers = CollectionUtils.isEmpty(leagueDTO.getLeaguePlayers()) ?
                    List.of(new LeaguePlayerDTO(
                            createdLeague.getLeagueId(),
                            createdLeague.getOwnerUsername(),
                            0,
                            PlayerStatus.ACTIVE)
                    ) :
                    leagueDTO.getLeaguePlayers().stream()
                            .map(player -> new LeaguePlayerDTO(
                                    createdLeague.getLeagueId(),
                                    player.getUsername(),
                                    player.getTotalPoints(),
                                    player.getPlayerStatus()))
                            .toList();

            leaguePlayerService.createLeaguePlayers(leaguePlayers);

            // leaguesService.storeInKafka(ChangeType.CREATE, createdLeague.getLeagueId(), createdLeague);

            return new ResponseEntity<>(createdLeague.getLeagueId(), HttpStatus.CREATED);
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/close/{id}")
    public ResponseEntity<?> closeLeagueById(@PathVariable Long id) {
        try {
            leagueService.closeLeagueById(id);

//            leaguesService.storeInKafka(ChangeType.CREATE, createdLeague.getLeagueId(), createdLeague);

            return ResponseEntity.ok().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/close/names/{name}")
    public ResponseEntity<?> closeLeagueByName(@PathVariable String name) {
        try {
            leagueService.closeLeagueByName(name);

//            leaguesService.storeInKafka(ChangeType.CREATE, createdLeague.getLeagueId(), createdLeague);

            return ResponseEntity.ok().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteLeague(@PathVariable Long id) {
        try {
            leagueService.deleteLeague(id);

//            leagueService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/names/{name}")
    public ResponseEntity<?> deleteLeagueByName(@PathVariable String name) {
        try {
            leagueService.deleteLeagueByName(name);

//            leagueService.storeInKafka(ChangeType.DELETE, id, null);

            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
