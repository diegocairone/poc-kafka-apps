package com.cairone.poc;

import com.cairone.poc.event.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Application started");
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "debezium-1", containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupFoo(Event event) {
        log.info("Received Message: {}", event);
    }
}
