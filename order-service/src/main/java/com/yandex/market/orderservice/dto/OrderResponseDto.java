package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.OrderStatus;
import com.yandex.market.orderservice.model.PaymentType;
import com.yandex.market.orderservice.model.ReceiptMethod;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema
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