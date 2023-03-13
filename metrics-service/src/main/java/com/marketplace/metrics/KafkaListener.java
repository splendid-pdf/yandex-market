package com.marketplace.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.metrics.model.Metric;
import com.marketplace.metrics.repo.MetricsRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListener {

    private final MetricsRepo metricsRepo;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @org.springframework.kafka.annotation.KafkaListener(
            topics = "METRICS",
            groupId = "product"
    )
    void listener(String data){
        var metric = objectMapper.readValue(data, Metric.class);
        log.info("Saving metric into database, {}", metric);
        metricsRepo.save(metric);
    }
}
