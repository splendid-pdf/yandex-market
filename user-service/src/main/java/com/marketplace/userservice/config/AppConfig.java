package com.marketplace.userservice.config;

import com.marketplace.userservice.config.properties.ErrorInfoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.validation")
    public ErrorInfoProperties errorInfoProperties() {
        return new ErrorInfoProperties();
    }
}