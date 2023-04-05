package com.marketplace.aggregatorservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate getRestTemplate(@Value("${app.rest.connect-timeout}") Integer connectTimeout,
                                        @Value("${app.rest.read-timeout}") Integer readTimeout) {

        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.of(connectTimeout, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(readTimeout, ChronoUnit.SECONDS))
                .build();
    }



}