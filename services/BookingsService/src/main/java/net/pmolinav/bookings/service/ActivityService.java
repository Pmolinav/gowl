package net.pmolinav.bookings.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pmolinav.bookings.producer.MessageProducer;
import net.pmolinav.bookings.repository.ActivityRepository;
import net.pmolinav.bookingslib.dto.ActivityDTO;
import net.pmolinav.bookingslib.dto.ChangeType;
import net.pmolinav.bookingslib.exception.InternalServerErrorException;
import net.pmolinav.bookingslib.exception.NotFoundException;
import net.pmolinav.bookingslib.mapper.ActivityMapper;
import net.pmolinav.bookingslib.model.Activity;
import net.pmolinav.bookingslib.model.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@EnableAsync
@Service
public class ActivityService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final MessageProducer messageProducer;

    private final String KAFKA_TOPIC = "my-topic";

    @Autowired
    public ActivityService(ActivityRepository activityRepository, ActivityMapper activityMapper, MessageProducer messageProducer) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
        this.messageProducer = messageProducer;
    }

    @Transactional(readOnly = true)
    public List<Activity> findAllActivities() {
        List<Activity> activityList;
        try {
            activityList = activityRepository.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error while searching all activities in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
        if (CollectionUtils.isEmpty(activityList)) {
            logger.warn("No activities were found in repository.");
            throw new NotFoundException("No activities found in repository.");
        } else {
            logger.debug("Activities are returned OK from repository.");
            return activityList;
        }
    }

    @Transactional
    public Activity createActivity(ActivityDTO activityDTO) {
        try {
            Activity activity = activityMapper.activityDTOToActivityEntity(activityDTO);
            return activityRepository.save(activity);
        } catch (Exception e) {
            logger.error("Unexpected error while creating new activity in repository.", e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Activity findByName(String name) {
        try {
            return activityRepository.findById(name)
                    .orElseThrow(() -> new NotFoundException(String.format("Activity with name %s does not exist.", name)));
        } catch (NotFoundException e) {
            logger.error("Activity with name {} was not found.", name, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while searching activity with name {} in repository.", name, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Transactional
    public void deleteActivity(String name) {
        try {
            Activity activity = activityRepository.findById(name)
                    .orElseThrow(() -> new NotFoundException(String.format("Activity with name %s does not exist.", name)));

            activityRepository.delete(activity);
        } catch (NotFoundException e) {
            logger.error("Activity with name {} was not found.", name, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while removing activity with name {} in repository.", name, e);
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Async
    public void storeInKafka(ChangeType changeType, String name, Activity activity) {
        try {
            messageProducer.sendMessage(this.KAFKA_TOPIC, new History(
                    new Date(),
                    changeType,
                    "Activity",
                    name,
                    activity == null ? null : new ObjectMapper().writeValueAsString(activity), // TODO: USE JSON PATCH.
                    "Admin" // TODO: createUser is not implemented yet.
            ));
        } catch (Exception e) {
            logger.warn("Kafka operation {} with name {} and activity {} need to be reviewed", changeType, name, activity);
        }
    }
}
