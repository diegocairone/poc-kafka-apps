package com.cairone.poc.common;

import com.cairone.poc.avro.payload.MessagePayload;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CustomAvroSerializer implements Serializer<MessagePayload> {

    private final Schema schema;

    public CustomAvroSerializer() {
        schema = new MessagePayload().getSchema();
    }

    @Override
    public byte[] serialize(String topic, MessagePayload data) {
        return serializeJsonEncoder(data);
    }

    private byte[] serializeJsonEncoder(MessagePayload data) {
        if (data == null) {
            return new byte[]{};
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            JsonEncoder encoder = EncoderFactory.get().jsonEncoder(schema, out, true);
            // to encode in binary format, use the following line instead
            // BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null)

            DatumWriter<MessagePayload> writer = new SpecificDatumWriter<>(schema);
            writer.write(data, encoder);
            encoder.flush();
            return out.toByteArray();

        } catch (IOException e) {
            throw new SerializationException("Error serializing Avro message", e);
        }
    }
}
