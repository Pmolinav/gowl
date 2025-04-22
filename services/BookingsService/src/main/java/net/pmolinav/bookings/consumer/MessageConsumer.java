package net.pmolinav.bookings.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.pmolinav.bookings.service.HistoryService;
import net.pmolinav.bookingslib.model.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private final HistoryService historyService;

    public MessageConsumer(HistoryService historyService) {
        this.historyService = historyService;
    }

    @KafkaListener(topics = "my-topic", groupId = "my-group-id")
    public void listen(String message) {
        try {
            historyService.createHistory(new ObjectMapper().readValue(message, History.class));
        } catch (Exception e) {
            logger.warn("Kafka operation with message {} failed in consumer and need to be reviewed", message);
        }
    }

}