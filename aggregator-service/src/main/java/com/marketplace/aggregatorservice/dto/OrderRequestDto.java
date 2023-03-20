package com.marketplace.aggregatorservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestDto(
        PaymentType paymentType,
        double price,
        boolean paid,
        LocalDateTime paymentDateTime,
        ReceiptMethodRequestDto receiptMethod,
        List<OrderedProductDto> orderedProducts) {
}