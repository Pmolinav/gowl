package com.pmolinav.leagues.clients;

import com.pmolinav.leagueslib.dto.LeagueCategoryDTO;
import com.pmolinav.leagueslib.dto.LeagueDTO;
import com.pmolinav.leagueslib.model.LeagueCategory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "LeagueCategoriesClient", url = "leaguesservice:8004/categories")
public interface LeagueCategoriesClient {

    @GetMapping
    List<LeagueCategory> findAllLeagueCategories();

    @GetMapping("/{id}")
    LeagueDTO findLeagueCategoryById(@PathVariable String id);

    @PostMapping
    String createLeagueCategory(@RequestBody LeagueCategoryDTO leagueCategoryDTO);

    @DeleteMapping("/{id}")
    void deleteLeague(@PathVariable String id);
}
