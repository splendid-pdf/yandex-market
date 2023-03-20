package com.marketplace.aggregatorservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


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