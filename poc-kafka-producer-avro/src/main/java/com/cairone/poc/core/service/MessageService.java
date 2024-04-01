package com.cairone.poc.core.service;

import com.cairone.poc.avro.record.MessageRecord;
import com.cairone.poc.core.model.MessageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final KafkaTemplate<UUID, MessageRecord> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String kafkaTopic;

    public MessageModel sendMessage(String message, int partition) {
        UUID key = UUID.randomUUID();
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setMessage(message);
        //messageRecord.setCount(100);
        kafkaTemplate.send(kafkaTopic, partition, key, messageRecord);
        log.info("Sent [key {}]: {}", key, messageRecord);
        return MessageModel.builder()
                .withMessage(messageRecord.getMessage().toString())
                .build();
    }
}
