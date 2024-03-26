package com.cairone.poc.common.serialization;

import com.cairone.poc.core.model.FooRecord;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class FooDeserializer implements Deserializer<FooRecord> {

    @Override
    public FooRecord deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (FooRecord) ois.readObject();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize object type", ex);
        }
    }
}
