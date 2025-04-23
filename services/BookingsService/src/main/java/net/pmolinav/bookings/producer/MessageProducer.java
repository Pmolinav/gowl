package com.pmolinav.bookings.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmolinav.userslib.model.History;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {
    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, History history) {
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(history));
        } catch (JsonProcessingException e) {
            logger.error("Unexpected error occurred while trying to parse the following historical message: {}", history);
            throw new RuntimeException(e);
        }
    }

}