package com.example.aisupport;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ser.jdk.StringSerializer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AisupportApplication {

	public static void main(String[] args) {
		SpringApplication.run(AisupportApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}

	@Bean
	public KafkaTemplate kafkaTemplate()
	{
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ProducerFactory<String,String> producerFactory()
	{
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}
}
