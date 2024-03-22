package com.cairone.poc;

import com.cairone.poc.core.model.FooRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class FooRecordProducerTask implements Runnable {
    private final KafkaTemplate<UUID, FooRecord> kafkaTemplate;
    private final String topic;

    @Override
    public void run() {

        log.info("Producer started");

        while (true) {

            UUID key = UUID.randomUUID();
            FooRecord fooRecord = FooRecord.builder()
                    .withId(key)
                    .withName("Hello Kafka for key: " + key)
                    .withCreatedAt(LocalDateTime.now())
                    .build();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            CompletableFuture<SendResult<UUID, FooRecord>> completableFuture =
                    kafkaTemplate.send(topic, key, fooRecord);

            completableFuture.whenComplete((sr, ex) -> {
                if (ex != null) {
                    log.error("Failed to send message", ex);
                } else {
                    log.info("Sent(key={},partition={}): {}",
                            sr.getProducerRecord().key(),
                            sr.getProducerRecord().partition(),
                            sr.getProducerRecord().value());
                }
            });
        }
    }
}
