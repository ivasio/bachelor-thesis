package com.ivasio.bachelor_thesis.shared.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;



public class KafkaConsumerConfig extends KafkaConfig{
    public KafkaConsumerConfig() {
        super();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "route_processor");
    }
}
