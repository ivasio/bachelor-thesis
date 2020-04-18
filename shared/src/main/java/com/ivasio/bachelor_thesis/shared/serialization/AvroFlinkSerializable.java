package com.ivasio.bachelor_thesis.shared.serialization;


import org.apache.flink.table.descriptors.Schema;

public interface AvroFlinkSerializable {
    Schema getFlinkSchema();
}
