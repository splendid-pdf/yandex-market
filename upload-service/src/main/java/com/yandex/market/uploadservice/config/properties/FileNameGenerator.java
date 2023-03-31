package com.yandex.market.uploadservice.config.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FileNameGenerator {

    private AtomicInteger counter = new AtomicInteger(1);

    @Bean
    public String generateFileName() {
        return "id" + counter.getAndIncrement();
    }
}