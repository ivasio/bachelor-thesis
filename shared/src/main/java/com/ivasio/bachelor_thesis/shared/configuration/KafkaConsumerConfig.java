package com.ivasio.bachelor_thesis.shared.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Map;


public class KafkaConsumerConfig extends KafkaConfig{
    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = super.getProperties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "route_processor");
        return properties;
    }
}
