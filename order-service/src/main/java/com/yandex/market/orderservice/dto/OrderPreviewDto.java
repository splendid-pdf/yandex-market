package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderPreviewDto(UUID externalId,
                              OrderStatus status,
                              double price,
                              boolean paid,
                              int quantity,
                              LocalDateTime creationTimestamp,
                              ReceiptMethodResponseDto receiptMethod,
                              List<OrderedProductPreviewDto> orderedProductPreviews
                              ) {
}
