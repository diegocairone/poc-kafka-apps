package com.cairone.poc;

import com.cairone.poc.avro.payload.MessagePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.UUID;

@Slf4j
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @KafkaListener(
            topics = "${app.kafka.topic}",
            groupId = "a",
            containerFactory = "kafkaListenerContainerFactory",
            concurrency = "3"
    )
    public void listen(
            @Header(KafkaHeaders.RECEIVED_KEY) UUID key, @Payload MessagePayload payload) {

        log.info("Received message with key {} and value: {}", key, payload);
    }
}
