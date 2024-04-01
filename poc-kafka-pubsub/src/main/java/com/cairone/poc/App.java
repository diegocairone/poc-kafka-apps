package com.cairone.poc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

        produceRecords(true);
        consumeRecords(false);

        while (true) {
            log.info("Application started");
            Thread.sleep(1000L * 60L * 60L * 24L);
        }
    }

    public void produceRecords(boolean execute) {
        if (execute) {
            // sending records to Kafka
            executorService.execute(new FooRecordProducerTask(topic));
        }
    }

    public void consumeRecords(boolean execute) {
        if (execute) {
            String groupId = String.format("consumer-foo-record-consumer-%s-group", UUID.randomUUID());
            // consuming records from Kafka
            executorService.execute(new FooRecordConsumerTask(topic, groupId));
            executorService.execute(new FooRecordConsumerTask(topic, groupId));
            executorService.execute(new FooRecordConsumerTask(topic, groupId));
            executorService.execute(new FooRecordConsumerTask(topic, groupId));
            executorService.execute(new FooRecordConsumerTask(topic, groupId));
            executorService.execute(new FooRecordConsumerTask(topic, groupId));
        }
    }

    @Value("${app.kafka.topic}")
    private String topic;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);
}
