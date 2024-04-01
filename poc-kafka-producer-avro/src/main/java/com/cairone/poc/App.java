package com.cairone.poc;

import com.cairone.poc.avro.record.MessageRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "a", containerFactory = "kafkaListenerContainerFactory")
    public void listen(MessageRecord genericRecord) {
        log.info("Received message with value: {}", genericRecord);
    }
}
