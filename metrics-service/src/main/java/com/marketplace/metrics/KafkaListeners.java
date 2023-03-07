package com.marketplace.metrics;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(
            topics = "METRICS",
            groupId = "groupId"
    )
    void listener(String data){
        System.out.println(data);
    }
}
