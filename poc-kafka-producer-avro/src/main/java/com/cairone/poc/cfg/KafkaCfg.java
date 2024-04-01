package com.cairone.poc.cfg;

import com.cairone.poc.avro.payload.MessagePayload;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
                .replicas(3)
                .build();
    }

    @Bean
    public ConsumerFactory<UUID, MessagePayload> consumerFactory(
            @Value("${app.kafka.bootstrap-servers}") final String bootstrapServers,
            @Value("${app.kafka.schema.registry.url}") final String schemaRegistryUrl) {

        final Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-consumer-group");
        // Spring class to cleanly handle deserialization errors
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, UUIDDeserializer.class);
        // The Confluent class to deserialize messages in the Avro format
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, KafkaAvroDeserializer.class);
        // The URL to the Confluent Schema Registry
        config.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        // Whether schemas that do not yet exist should be registered
        config.put(AbstractKafkaAvroSerDeConfig.AUTO_REGISTER_SCHEMAS, false);
        // Deserialize to the generated Avro class rather than a GenericRecord type
        config.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, MessagePayload> kafkaListenerContainerFactory(
            @Value("${app.kafka.bootstrap-servers}") final String bootstrapServers,
            @Value("${app.kafka.schema.registry.url}") final String schemaRegistryUrl) {

        ConcurrentKafkaListenerContainerFactory<UUID, MessagePayload> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(bootstrapServers, schemaRegistryUrl));
        return factory;
    }
}
