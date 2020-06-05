package com.ivasio.bachelor_thesis.shared.configuration;

import com.ivasio.bachelor_thesis.shared.records.AvroGenericRecordSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;


public class KafkaProducerConfig extends KafkaConfig {
    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = super.getProperties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroGenericRecordSerializer.class);
        return properties;
    }
}
