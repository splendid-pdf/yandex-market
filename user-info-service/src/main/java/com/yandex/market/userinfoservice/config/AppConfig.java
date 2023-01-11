package com.yandex.market.userinfoservice.config;

import com.yandex.market.userinfoservice.config.properties.ErrorInfoProperties;
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

//    @Bean
//    public ObjectMapper objectMapper() {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.registerModule(new JavaTimeModule());
//
//        return objectMapper;
//    }
}