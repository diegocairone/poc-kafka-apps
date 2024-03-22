package com.cairone.poc;

import com.cairone.poc.core.model.FooRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //produceRecords();

        while (true) {
            log.info("Application started");
            Thread.sleep(1000 * 60 * 60 * 24);
        }
    }

    public void produceRecords() {
        // sending records to Kafka
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new FooRecordProducerTask(kafkaTemplate, topic));
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "foo")
    public void listenGroupFoo(FooRecord record) {
        log.info("Received Message in group foo: {}", record);
    }

    private final KafkaTemplate<UUID, FooRecord> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topic;
}
