package com.cairone.poc.core.service;

import com.cairone.poc.core.model.MessageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    @KafkaListener(
            topicPartitions = @TopicPartition(
                    topic = "${app.kafka.topic}", partitions = "0"),
            groupId = "${app.kafka.group-id}")
    public void listen(ConsumerRecord<UUID, MessageModel> in) {
        log.info("Received at Consumer 1 [Key {}]: {}", in.key(), in.value());
    }
}
