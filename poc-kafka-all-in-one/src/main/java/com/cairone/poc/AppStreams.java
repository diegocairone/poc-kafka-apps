package com.cairone.poc;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

public class AppStreams {

    public static void main(String[] args) {
        AppStreams app = new AppStreams();
        app.run();
    }

    public void run() {

        Properties streamsProps = getProperties();
        StreamsBuilder builder = new StreamsBuilder();

        final String inputTopic = "input-topic";
        final String outputTopic = "output-topic";
        final String orderNumberStart = "orderNumber-";

        KStream<String, String> source = builder.stream(
                inputTopic, Consumed.with(Serdes.String(), Serdes.String()));

        source.peek((k,v) -> System.out.println("Received: " + k + " : " + v))
                .to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams streams = new KafkaStreams(builder.build(), streamsProps);
        streams.start();

        while (true) {}
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-test");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return props;
    }
}
