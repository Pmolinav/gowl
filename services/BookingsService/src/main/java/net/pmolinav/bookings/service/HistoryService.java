package net.pmolinav.bookings.service;

import net.pmolinav.bookings.repository.HistoryRepository;
import net.pmolinav.bookingslib.exception.InternalServerErrorException;
import net.pmolinav.bookingslib.model.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistoryService {

    private static final Logger logger = LoggerFactory.getLogger(HistoryService.class);

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Transactional
    public History createHistory(History history) {
        try {
            return historyRepository.save(history);
        } catch (Exception e) {
            logger.error("Unexpected error while creating new history: {} in repository.", history.toString(), e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
