package com.yandex.market.userservice.config;

import com.yandex.market.userservice.config.properties.ErrorInfoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.validation")
    public ErrorInfoProperties errorInfoProperties() {
        return new ErrorInfoProperties();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }
}