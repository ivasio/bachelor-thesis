package com.ivasio.bachelor_thesis.shared.configuration;

import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;


public class KafkaConfig {
    protected final SystemConfiguration systemConfig = new SystemConfiguration();
    protected final Properties properties = new Properties();

    public KafkaConfig() {
        super();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, systemConfig.getString("KAFKA_BOOTSTRAP_SERVERS"));
    }

    public Properties getProperties() {
        return properties;
    }
}
