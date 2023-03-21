package com.marketplace.aggregatorservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
        UUID externalId,
        UUID userId,
        OrderStatus orderStatus,
        PaymentType paymentType,
        double price,
        boolean paid,
        LocalDateTime paymentDateTime,
        LocalDateTime creationTimestamp,
        ReceiptMethod receiptMethod,
        List<OrderedProductDto> orderedProducts) {
}