package com.yandex.market.productservice.metric.dto;

import com.yandex.market.productservice.metric.enums.UserAction;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductMetricsDto {
    private UserAction userAction;
    private String userId;
    private UUID productExternalId;
    private String productName;
    private LocalDateTime timestamp;
}