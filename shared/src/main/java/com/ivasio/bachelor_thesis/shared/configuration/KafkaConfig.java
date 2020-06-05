package com.ivasio.bachelor_thesis.shared.configuration;

import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.HashMap;
import java.util.Map;


public class KafkaConfig {
    protected final SystemConfiguration systemConfig = new SystemConfiguration();

    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, systemConfig.getString("KAFKA_BOOTSTRAP_SERVERS"));
        return properties;
    }
}
