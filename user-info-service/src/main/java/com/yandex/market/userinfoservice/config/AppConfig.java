package com.yandex.market.userinfoservice.config;

import com.yandex.market.userinfoservice.config.properties.ErrorInfoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
//    @ConditionalOnProperty(prefix = "app.validation",)
    @ConfigurationProperties(prefix = "app.validation")
    public ErrorInfoProperties errorInfoProperties() {
        return new ErrorInfoProperties();
    }
}
