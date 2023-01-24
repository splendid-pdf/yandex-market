package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.DeliveryMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReceiptMethodRequestDto(
        @NotNull(message = "DeliveryMethod field must not be empty")
        @Valid
        DeliveryMethod deliveryMethod,
        @NotNull(message = "Address field must not be empty")
        @Valid
        AddressRequestDto address,
        @NotBlank(message = "ReceiverName field must not be empty")
        String receiverName,
        @NotBlank(message = "ReceiverName field must not be empty")
        String receiverPhone,
        @Email(message = "ReceiverEmail should be valid")
        String receiverEmail,
        @NotNull(message = "DeliveryDate field must not be empty")
        @Future(message = "DeliveryDate field must be future")
        LocalDate deliveryDate,
        @NotNull(message = "DeliveryStart field must not be empty")
        @PastOrPresent(message = "DeliveryStart field must be past or present")
        LocalTime deliveryStart,
        @NotNull(message = "DeliveryEnd field must not be empty")
        @Future(message = "DeliveryEnd field must be future")
        LocalTime deliveryEnd,
        @PositiveOrZero(message = "DeliveryCost must be positive or 0")
        double deliveryCost) {
}