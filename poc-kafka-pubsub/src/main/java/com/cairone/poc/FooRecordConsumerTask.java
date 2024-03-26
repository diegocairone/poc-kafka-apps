package com.cairone.poc;

import com.cairone.poc.common.serialization.FooDeserializer;
import com.cairone.poc.core.model.FooRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.UUIDDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

@Slf4j
public class FooRecordConsumerTask implements Runnable {

    private final String topic;
    private final String groupId;
    private final KafkaConsumer<UUID, FooRecord> consumer;

    public FooRecordConsumerTask(String topic, String groupId) {
        this.topic = topic;
        this.groupId = groupId;
        this.consumer = new KafkaConsumer<>(getProperties());
    }

    public void run() {
        consumer.subscribe(Collections.singletonList(topic));

        while (true) {
            ConsumerRecords<UUID, FooRecord> consumerRecords = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<UUID, FooRecord> message : consumerRecords) {
                log.info("Consumed: {} -> {} on thread: {}",
                        message.key(),
                        message.value(),
                        Thread.currentThread().getName());
            }
            // to reset the existing consumer to read from the beginning of the topic,
            // we use the KafkaConsumer.seekToBeginning(Collection<TopicPartition> partitions) method
            // use: consumer.seekToBeginning(consumer.assignment())
            // consumer.assignment() returns the set of partitions currently assigned to the consumer
        }
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19091,localhost:29092,localhost:39093");

        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // to read all records do this:
        // be sure that the consumer group is new, otherwise it will start from the last offset
        // props.put(ConsumerConfig.GROUP_ID_CONFIG, String.format("foo-record-consumer-%s-group", UUID.randomUUID()))

        // makes the consumer to read from the beginning of the topic
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // alternatively, to read from the end of the topic
        //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, FooDeserializer.class.getName());
        return props;
    }
}
