package com.ivasio.bachelor_thesis.shared.records;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;


public class AvroGenericRecordSerializer implements Serializer<GenericRecord> {

    private Schema schema = null;

    @Override public void configure(Map<String, ?> map, boolean b) {
        schema = (Schema) map.get("SCHEMA");
    }

    @Override public byte[] serialize(String arg0, GenericRecord record) { //List<GenericRecord> records) {
        byte[] retVal = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GenericDatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);

        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
        try {
            dataFileWriter.create(schema, outputStream);
            dataFileWriter.append(record);
            dataFileWriter.flush();
            dataFileWriter.close();
            retVal = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override public void close() {}

}