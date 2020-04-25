package com.ivasio.bachelor_thesis.shared.records;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

abstract class AvroRecord {
    abstract public Schema getSchema();
    abstract public GenericRecord toGenericRecord();

    public AvroRecord() {}
}
