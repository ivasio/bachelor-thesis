package com.ivasio.bachelor_thesis.shared.records;

import com.ivasio.bachelor_thesis.shared.models.Junction;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;


public class JunctionUpdate extends AvroRecord<JunctionUpdate> {
    public long id;
    public float longitude;
    public float latitude;
    public float radius;

    @Override
    public String getTopicName() {
        return "source_junctions";
    }

    @Override
    public Schema getSchema() {
        return SchemaBuilder.record("JunctionUpdate")
            .namespace("com.ivasio.bachelor_thesis.shared.records")
            .fields()
                .requiredLong("id")
                .requiredFloat("longitude")
                .requiredFloat("latitude")
                .requiredFloat("radius")
            .endRecord();
    }

    @Override
    public GenericRecord toGenericRecord() {
        GenericRecord record = new GenericData.Record(getSchema());
        record.put("id", id);
        record.put("longitude", longitude);
        record.put("latitude", latitude);
        record.put("radius", radius);
        return record;
    }

    @Override
    public JunctionUpdate fromGenericRecord(GenericRecord genericRecord) {
        return new JunctionUpdate(
            (long)genericRecord.get("id"),
            (float)genericRecord.get("longitude"),
            (float)genericRecord.get("latitude"),
            (float)genericRecord.get("radius")
        );
    }

    public JunctionUpdate() {}

    public JunctionUpdate(Junction junction) {
        this(junction.getId(), junction.getLongitude(), junction.getLatitude(), junction.getRadius());
    }

    public JunctionUpdate(long id, float longitude, float latitude, float radius) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    public boolean containsPoint(SourcedPoint point) {
        float longitudeDiff = longitude - point.longitude;
        float latitudeDiff = latitude - point.latitude;
        return longitudeDiff * longitudeDiff + latitudeDiff * latitudeDiff <= radius * radius;
    }

}
