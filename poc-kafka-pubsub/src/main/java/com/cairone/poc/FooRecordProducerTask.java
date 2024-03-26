package com.cairone.poc;

import com.cairone.poc.core.model.FooRecord;
import com.cairone.poc.core.model.FooSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.UUIDSerializer;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
public class FooRecordProducerTask implements Runnable {

    private final String topic;
    private final AdminClient adminClient = AdminClient.create(getProperties());

    @Override
    public void run() {
        createIfNotExists();
        sendMessage(10, 1);
    }

    public void createIfNotExists() {
        try {
            boolean exists = adminClient.listTopics().names().get().contains(topic);
            if (!exists) {
                log.info("Creating topic: {}", topic);
                NewTopic newTopic = new NewTopic(topic, 5, (short) 1);
                adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
                log.info("Topic created");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void createIfNotExistsAsync() {

        adminClient.listTopics().names().whenComplete((topics, e) -> {
            if (e != null) {
                log.error("Error listing topics", e);
            } else {
                if (topics.contains(topic)) {
                    log.info("Topic already exists");
                } else {

                    log.info("Creating topic: {}", topic);
                    NewTopic newTopic = new NewTopic(topic, 5, (short) 1);

                    CreateTopicsResult result = adminClient.createTopics(Collections.singletonList(newTopic));
                    result.all().whenComplete((v, e2) -> {
                        if (e2 != null) {
                            log.error("Error creating topic", e2);
                        } else {
                            log.info("Topic created");
                        }
                    });
                }
            }
        });
    }

    public void sendMessage(int count, int each) {

        log.info("Producer started");
        int iter = 0;

        while (iter < count) {

            UUID key = UUID.randomUUID();
            FooRecord fooRecord = FooRecord.builder()
                    .withId(key)
                    .withName("Hello Kafka for key: " + key)
                    .withCreatedAt(LocalDateTime.now())
                    .build();

            try {
                Thread.sleep(1000L * each);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            KafkaProducer<UUID, FooRecord> producer = new KafkaProducer<>(getProperties());

            ProducerRecord<UUID, FooRecord> message = new ProducerRecord<>(topic, key, fooRecord);

            producer.send(message, (metadata, exception) -> {
                if (exception == null) {
                    log.info("Message sent to topic: {} in partition {} @ {}",
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset());
                } else {
                    log.error("Error sending message", exception);
                }
            });

            iter++;
        }
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092,localhost:39092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, "0");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, FooSerializer.class.getName());
        return props;
    }
}
