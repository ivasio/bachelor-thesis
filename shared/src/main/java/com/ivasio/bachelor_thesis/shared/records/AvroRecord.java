package com.ivasio.bachelor_thesis.shared.records;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

abstract public class AvroRecord<RecordType> {
    abstract public String getTopicName();
    abstract public Schema getSchema();
    abstract public GenericRecord toGenericRecord();
    abstract public RecordType fromGenericRecord(GenericRecord genericRecord);
}
