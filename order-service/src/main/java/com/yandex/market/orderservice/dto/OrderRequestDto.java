package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema
public record OrderRequestDto(
        @NotNull(message = "Способ оплаты должен быть заполнен")
        PaymentType paymentType,
        @PositiveOrZero(message = "Цена заказа не может быть отрицательной")
        double price,
        boolean paid,
        LocalDateTime paymentDateTime,
        @Valid
        @NotNull(message = "Способ получения должен быть заполнен")
        ReceiptMethodRequestDto receiptMethod,
        @NotNull(message = "Заказанный пользователем продукт должен быть заполнен")
        List<@Valid OrderedProductDto> orderedProducts) {
}