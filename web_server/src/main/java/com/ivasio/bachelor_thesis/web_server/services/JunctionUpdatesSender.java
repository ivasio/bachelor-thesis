package com.ivasio.bachelor_thesis.web_server.services;

import com.ivasio.bachelor_thesis.shared.configuration.JunctionUpdateKafkaProducerConfig;
import com.ivasio.bachelor_thesis.shared.models.Junction;
import com.ivasio.bachelor_thesis.shared.records.JunctionUpdate;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

public class JunctionUpdatesSender {
    private final Properties properties;
    private final KafkaProducer<String, GenericRecord> producer;

    JunctionUpdatesSender () {
        properties = new JunctionUpdateKafkaProducerConfig().getProperties();
        producer = new KafkaProducer<>(properties);
    }

    RecordMetadata send(Junction junction) {
        Future<RecordMetadata> future = producer.send(new ProducerRecord<>(
                properties.getProperty("TOPIC_NAME"), new JunctionUpdate(junction).toGenericRecord()
        ));

        try {
            return future.get();
        } catch (Exception exc) {
            System.err.println(exc.getLocalizedMessage());
            return null;
        }
    }
}
