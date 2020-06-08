package com.ivasio.bachelor_thesis.shared.configuration;

import com.ivasio.bachelor_thesis.shared.records.JunctionUpdate;

import java.util.Properties;


public class JunctionUpdateKafkaProducerConfig extends KafkaProducerConfig{
    @Override
    public Properties getProperties() {
        Properties properties = super.getProperties();
        properties.put("SCHEMA", new JunctionUpdate().getSchema());
        properties.put("TOPIC_NAME", "source_junctions");
        return properties;
    }
}
