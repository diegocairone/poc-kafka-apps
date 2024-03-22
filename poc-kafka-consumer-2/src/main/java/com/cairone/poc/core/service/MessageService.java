package com.cairone.poc.core.service;

import com.cairone.poc.core.model.MessageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final KafkaTemplate<String, MessageModel> kafkaTemplate;

    @KafkaListener(
            topics = "topic1",
            groupId = "msg-consumer-group-2")
    public void listen(MessageModel in) {
        log.info("Received at Consumer 2: " + in);
    }
}
