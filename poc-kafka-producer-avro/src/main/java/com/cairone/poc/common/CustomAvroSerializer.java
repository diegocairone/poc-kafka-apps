package com.cairone.poc.common;

import com.cairone.poc.avro.record.MessageRecord;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CustomAvroSerializer implements Serializer<MessageRecord> {

    private final Schema schema;

    public CustomAvroSerializer() {
        schema = new MessageRecord().getSchema();
    }

    @Override
    public byte[] serialize(String topic, MessageRecord data) {
        return serializeJsonEncoder(data);
    }

    private byte[] serializeJsonEncoder(MessageRecord data) {
        if (data == null) {
            return new byte[]{};
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            JsonEncoder encoder = EncoderFactory.get().jsonEncoder(schema, out, true);
            // to encode in binary format, use the following line instead
            // BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null)

            DatumWriter<MessageRecord> writer = new SpecificDatumWriter<>(schema);
            writer.write(data, encoder);
            encoder.flush();
            return out.toByteArray();

        } catch (IOException e) {
            throw new SerializationException("Error serializing Avro message", e);
        }
    }
}
