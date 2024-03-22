package com.cairone.poc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Future;

@Slf4j
public class AppProducer1 {

    private final KafkaProducer<String, String> producer;

    public static void main(String[] args) {
        AppProducer1 app = new AppProducer1();
        app.run();
    }

    public AppProducer1() {
        this.producer = new KafkaProducer<>(getProducerProperties());
    }

    public void run() {
        try(Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String topic = readFromKeyboard(scanner, "Enter topic name", "input-topic");
                String key = readFromKeyboard(scanner, "Enter key", "");
                String value = readFromKeyboard(scanner, "Enter value", "value 02");
                send(topic, key, value);
            }
        }
    }

    private void send(String topic, String key, String value) {

        log.info("Starting producer...");

        if (key == null || key.isEmpty()) {
            key = RandomStringUtils.randomAlphabetic(5);
        }

        ProducerRecord<String, String> message = new ProducerRecord<>(topic, key, value);

        Future<RecordMetadata> future = producer.send(message, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Message sent to topic: " + metadata.topic() + " in partition " + metadata.partition() + " @ " + metadata.offset());
                log.info("Message sent to topic: {} in partition {} @ {}",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset());
            } else {
                exception.printStackTrace();
            }
        });
    }

    private Properties getProducerProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, "0");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
    }

    private String readFromKeyboard(Scanner scanner, String userMsg, String defaultValue) {

            System.out.println(userMsg);
            String topic = scanner.nextLine();

            if (topic == null || topic.isEmpty()) {
                System.out.println("Using default value: " + defaultValue);
                return defaultValue;
            }
            return topic;

    }
}
