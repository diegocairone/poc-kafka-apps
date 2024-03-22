package com.cairone.poc.cfg;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Slf4j
@Configuration
public class KafkaCfg {

    @Bean
    public NewTopic topic(
            @Value("${app.kafka.topic}") String topicName,
            @Value("${app.kafka.partitions}") int partitions) {

        log.info("Creating topic: " + topicName);
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(1)
                .build();
    }
}
