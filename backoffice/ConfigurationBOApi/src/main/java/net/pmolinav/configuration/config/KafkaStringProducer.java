package net.pmolinav.configuration.config;//package net.pmolinav.springboot.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.KafkaTemplate;
//
//@Configuration
//public class KafkaStringProducer {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStringProducer.class);
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public KafkaStringProducer(@Qualifier("kafkaStringTemplate") KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendMessage(String message) {
//        LOGGER.info("Producing message {}", message);
//        this.kafkaTemplate.send("TOPIC-DEMO", message);
//    }
//}
