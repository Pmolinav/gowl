package net.pmolinav.configuration.config;//package net.pmolinav.springboot.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//
//@Configuration
//public class KafkaStringConsumer {
//
//    Logger logger = LoggerFactory.getLogger(KafkaStringConsumer.class);
//
//    @KafkaListener(topics = "TOPIC-DEMO" , groupId = "group_id")
//    public void consume(String message) {
//        logger.info("Consuming Message {}", message);
//    }
//}
