package com.cairone.poc.common.serialization;

import com.cairone.poc.core.model.FooRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.util.SerializationUtils;

public class FooSerializer implements Serializer<FooRecord> {

    @Override
    public byte[] serialize(String topic, FooRecord data) {
        return SerializationUtils.serialize(data);
    }
}
