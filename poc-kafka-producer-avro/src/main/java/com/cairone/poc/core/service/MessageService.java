package com.cairone.poc.core.service;

import com.cairone.poc.avro.payload.MessagePayload;
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

    private final KafkaTemplate<UUID, MessagePayload> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String kafkaTopic;

    public MessageModel sendMessage(String message, int partition) {
        UUID key = UUID.randomUUID();
        MessagePayload payload = new MessagePayload();
        payload.setMessage(message);
        //messageRecord.setCount(100);
        kafkaTemplate.send(kafkaTopic, partition, key, payload);
        log.info("Sent [key {}]: {}", key, payload);
        return MessageModel.builder()
                .withMessage(payload.getMessage().toString())
                .build();
    }
}
