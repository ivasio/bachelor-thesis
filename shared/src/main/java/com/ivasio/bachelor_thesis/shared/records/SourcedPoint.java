package com.ivasio.bachelor_thesis.shared.records;

import com.ivasio.bachelor_thesis.shared.models.Point;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;

import java.time.Instant;


public class SourcedPoint extends AvroRecord<SourcedPoint> {
    public String sourceId;
    public float longitude;
    public float latitude;
    public Instant timestamp;

    @Override
    public String getTopicName() {
        return "source_points";
    }

    @Override
    public Schema getSchema() {
        return SchemaBuilder.record("SourcedPoint")
            .namespace("com.ivasio.bachelor_thesis.shared.records")
            .fields()
                .requiredFloat("longitude")
                .requiredFloat("latitude")
                .requiredLong("timestamp")
                .requiredString("sourceId")
            .endRecord();
    }

    @Override
    public GenericRecord toGenericRecord() {
        GenericRecord record = new GenericData.Record(getSchema());
        record.put("longitude", longitude);
        record.put("latitude", latitude);
        record.put("timestamp", timestamp.getEpochSecond());
        record.put("sourceId", sourceId);
        return record;
    }

    @Override
    public SourcedPoint fromGenericRecord(GenericRecord genericRecord) {
        return new SourcedPoint(
            ((Utf8)genericRecord.get("sourceId")).toString(),
            (float)genericRecord.get("longitude"),
            (float)genericRecord.get("latitude"),
            Instant.ofEpochSecond((long)genericRecord.get("timestamp"))
        );
    }

    public SourcedPoint() {}

    public SourcedPoint(String sourceId, float longitude, float latitude, Instant timestamp) {
        this.sourceId = sourceId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
    }

    public Point toPoint() {
        return new Point(latitude, longitude, timestamp);
    }

}
