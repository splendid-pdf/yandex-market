package com.yandexmarket.compositorservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderPreviews(
        String productName,
        String productImage,
        UUID article,
        int amount,
        LocalDateTime creationTimestamp,
        OrderStatus status,
        String receiverName,
        String receiverPhone,
        String receiverAddress
) {
}