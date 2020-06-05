package com.ivasio.bachelor_thesis.shared.configuration;

import com.ivasio.bachelor_thesis.shared.records.SourcedPoint;

import java.util.Map;


public class SourcedPointKafkaProducerConfig extends KafkaProducerConfig{
    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = super.getProperties();
        properties.put("SCHEMA", new SourcedPoint().getSchema());
        properties.put("TOPIC_NAME", "source_points");
        return properties;
    }
}
