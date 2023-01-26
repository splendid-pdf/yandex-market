package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema
public record OrderPreviewDto(
        UUID externalId,
        OrderStatus orderStatus,
        double price,
        boolean paid,
        int quantity,
        LocalDateTime creationTimestamp,
        ReceiptMethodPreviewDto receiptMethod,
        List<OrderedProductPreviewDto> orderedProductPreviews
) {
}