package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema
public record OrderRequest(

        @NotNull(message =  "Идентификатор продавца должен быть заполнен")
        UUID sellerId,
        @NotNull(message = "Способ оплаты должен быть заполнен")
        PaymentType paymentType,
        @PositiveOrZero(message = "Цена заказа не может быть отрицательной")
        Long price,
        boolean paid,
        LocalDateTime paymentDateTime,
        @Valid
        @NotNull(message = "Способ получения должен быть заполнен")
        ReceiptMethodRequest receiptMethod,
        @NotNull(message = "Заказанный пользователем продукт должен быть заполнен")
        List<@Valid OrderedProductDto> orderedProducts) {
}