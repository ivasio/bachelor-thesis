package com.ivasio.bachelor_thesis.shared.configuration;

import com.ivasio.bachelor_thesis.shared.records.JunctionUpdate;

import java.util.Map;


public class JunctionUpdateKafkaProducerConfig extends KafkaProducerConfig{
    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = super.getProperties();
        properties.put("SCHEMA", new JunctionUpdate().getSchema());
        properties.put("TOPIC_NAME", "source_kunctions");
        return properties;
    }
}
