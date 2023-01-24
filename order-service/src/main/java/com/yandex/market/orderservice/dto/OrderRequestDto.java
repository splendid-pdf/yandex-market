package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.PaymentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestDto(
        @NotNull(message = "PaymentType field must not be empty")
        PaymentType paymentType,
        @Positive(message = "Price must be greater than 0")
        double price,
        boolean paid,
        @NotNull(message = "PaymentDateTime field must not be empty")
        @FutureOrPresent(message = "PaymentDateTime delivery field must be future or present")
        LocalDateTime paymentDateTime,
        @NotNull(message = "CreationTimestamp field must not be empty")
        @PastOrPresent(message = "End time delivery field must be past or present")
        LocalDateTime creationTimestamp,
        @NotNull(message = "ReceiptMethod field must not be empty")
        @Valid
        ReceiptMethodRequestDto receiptMethod,
        List<@Valid OrderedProductDto> orderedProducts) {
}