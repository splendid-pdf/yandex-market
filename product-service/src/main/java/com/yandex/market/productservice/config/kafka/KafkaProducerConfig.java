package com.yandex.market.productservice.config.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
public class KafkaProducerConfig {

//    @Bean
//    public ProducerFactory<String, String> producerFactory(
//            @Value("${spring.kafka.ssl.keystore-location}") String location,
//            KafkaProperties properties
//    ) {
//        Map<String, Object> configs = properties.buildProducerProperties();
//        configs.replace("ssl.keystore.location", location);
//        configs.replace("ssl.truststore.location", location);
//        return new DefaultKafkaProducerFactory<>(configs);
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
//        return new KafkaTemplate<>(producerFactory);
//    }
}