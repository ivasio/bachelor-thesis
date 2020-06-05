package com.ivasio.bachelor_thesis.shared.configuration;

import com.ivasio.bachelor_thesis.shared.records.SourcedPoint;


public class SourcedPointKafkaProducerConfig extends KafkaProducerConfig{
    public SourcedPointKafkaProducerConfig() {
        super();
        properties.put("SCHEMA", new SourcedPoint().getSchema());
        properties.put("TOPIC_NAME", "source_points");
    }
}
