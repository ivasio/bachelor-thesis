package com.ivasio.bachelor_thesis.shared.configuration;

import com.ivasio.bachelor_thesis.shared.records.AvroGenericRecordSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;


public class KafkaProducerConfig extends KafkaConfig {
    public KafkaProducerConfig() {
        super();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroGenericRecordSerializer.class);
    }
}
