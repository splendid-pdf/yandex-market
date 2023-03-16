package com.marketplace.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public PathPatternParser parser() {
        return new PathPatternParser();
    }

    @Bean
    public List<PathPattern> pathPatterns(SecurityProperties properties, PathPatternParser parser) {
        return properties.getWhiteList().stream()
                .map(parser::parse)
                .toList();
    }
}