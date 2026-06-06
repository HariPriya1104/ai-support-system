package com.example.aisupport.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;

@Component
public class TicketKafkaProducer {

    private final KafkaProducer<String,String> producer;

    public TicketKafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "1000");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "1000");
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, "2000");
        this.producer = new KafkaProducer<>(props);
    }
    public void sendMessage(String topic, String message) {
        try {
            producer.send(new ProducerRecord<>(topic, message));
            System.out.println("Message sent: " + message);
        } catch (Exception e) {
            System.out.println("Kafka unavailable, skipping: " + e.getMessage());
        }
    }
}
