package com.pmolinav.predictions.services;

import com.pmolinav.predictions.clients.PredictionsServiceClient;
import com.pmolinav.shared.exceptions.CustomStatusException;
import com.pmolinav.shared.exceptions.InternalServerErrorException;
import com.pmolinav.shared.exceptions.NotFoundException;
import com.pmolinav.predictionslib.dto.OddsDTO;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OddsBOService {

    private static final Logger logger = LoggerFactory.getLogger(OddsBOService.class);

    @Autowired
    private PredictionsServiceClient predictionsServiceClient;

    public List<OddsDTO> findAll() {
        try {
            return predictionsServiceClient.findAllOdds();
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Odds not found.", e);
                throw new NotFoundException("Odds not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected error while fetching all odds", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public OddsDTO findById(Long id) {
        try {
            return predictionsServiceClient.findOddsById(id);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Odds with id {} not found.", id, e);
                throw new NotFoundException("Odds with id " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected error while fetching odds by id", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<OddsDTO> findByEventType(String type) {
        try {
            return predictionsServiceClient.findOddsByEventType(type);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Odds by event type {} not found.", type, e);
                throw new NotFoundException("Odds by event type " + type + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected error while fetching odds by eventType {}", type, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public List<OddsDTO> findOddsByMatchId(Long matchId) {
        try {
            return predictionsServiceClient.findOddsByMatchId(matchId);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Odds with match id {} not found.", matchId, e);
                throw new NotFoundException("Odds by matchId " + matchId + " not found");
            }
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while calling service.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Long create(OddsDTO dto) {
        try {
            return predictionsServiceClient.createOdds(dto);
        } catch (Exception e) {
            logger.error("Error while creating odds", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void update(Long id, OddsDTO dto) {
        try {
            predictionsServiceClient.updateOdds(id, dto);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Odds with id {} not found.", id, e);
                throw new NotFoundException("Odds with id " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Error while updating odds", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void delete(Long id) {
        try {
            predictionsServiceClient.deleteOdds(id);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Odds with id {} not found.", id, e);
                throw new NotFoundException("Odds with id " + id + " not found");
            }
        } catch (Exception e) {
            logger.error("Error while deleting odds", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public void deleteByMatchId(Long matchId) {
        try {
            predictionsServiceClient.deleteOddsByMatchId(matchId);
        } catch (FeignException e) {
            if (e.status() != 404) {
                logger.error("Unexpected error while calling service with status code {}.", e.status(), e);
                throw new CustomStatusException(e.getMessage(), e.status());
            } else {
                logger.warn("Odds by match id {} not found.", matchId, e);
                throw new NotFoundException("Odds by match id " + matchId + " not found");
            }
        } catch (Exception e) {
            logger.error("Error while deleting odds", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}