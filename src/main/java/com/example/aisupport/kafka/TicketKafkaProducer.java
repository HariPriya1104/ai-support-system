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

    public TicketKafkaProducer()
    {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.producer = new KafkaProducer<>(properties);
    }
    public void sendMessage(String topic, String message)
    {
        producer.send(new ProducerRecord<>(topic,message));
        System.out.println("Message send : " + message);
    }
}
