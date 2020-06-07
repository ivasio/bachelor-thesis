package com.ivasio.bachelor_thesis.shared.configuration;

import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;


public class KafkaConfig {
    protected final EnvironmentConfiguration systemConfig = new EnvironmentConfiguration();
    protected final Properties properties = new Properties();

    public KafkaConfig() {
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, systemConfig.getString("KAFKA_BOOTSTRAP_SERVERS"));
    }

    public Properties getProperties() {
        return properties;
    }
}
