package com.pmolinav.matchdatasync.services;

import com.pmolinav.matchdatasync.repositories.PlayerBetSelectionRepository;
import com.pmolinav.predictionslib.mapper.PlayerBetSelectionMapper;
import com.pmolinav.predictionslib.model.PlayerBetSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@EnableAsync
@Service
public class PlayerBetSelectionService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerBetSelectionService.class);

    private final PlayerBetSelectionRepository playerBetSelectionRepository;
    private final PlayerBetSelectionMapper playerBetSelectionMapper;

    @Autowired
    public PlayerBetSelectionService(PlayerBetSelectionRepository playerBetSelectionRepository,
                                     PlayerBetSelectionMapper playerBetSelectionMapper) {
        this.playerBetSelectionRepository = playerBetSelectionRepository;
        this.playerBetSelectionMapper = playerBetSelectionMapper;
    }


    @Transactional
    public void saveAll(List<PlayerBetSelection> selections) {
        playerBetSelectionRepository.saveAll(selections);
    }
}
