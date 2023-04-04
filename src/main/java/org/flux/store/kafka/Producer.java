package org.flux.store.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {

    public static final String KEY_SERIALIZER = "key.serializer";
    public static final String VALUE_SERIALIZER = "value.serializer";

    private KafkaProducer<String, String> client;
    private String topic;

    public Producer(Properties properties, String topic) {
        properties.setProperty(KEY_SERIALIZER, StringSerializer.class.getName());
        properties.setProperty(VALUE_SERIALIZER, StringSerializer.class.getName());
        this.client = new KafkaProducer<>(properties);
        this.topic = topic;
    }

    public void sendMessage(String json) {
        ProducerRecord<String, String> record = new ProducerRecord<>(this.topic, json);
        this.client.send(record);
        this.client.flush();
    }

    public void kill() {
        this.client.close();
    }
}
