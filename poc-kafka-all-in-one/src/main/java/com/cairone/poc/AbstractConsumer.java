package com.cairone.poc;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public abstract class AbstractConsumer {

    protected final KafkaConsumer<String, String> consumer;

    protected AbstractConsumer(Properties properties) {
        this.consumer = new KafkaConsumer<>(properties);
    }

    protected void run(String topic) {

        consumer.subscribe(Collections.singletonList(topic));

        while (true) {
            ConsumerRecords<String, String> messages = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : messages) {
                System.out.println("Consumed: " + record.key() + " -> " + record.value());
            }
        }
    }
}
