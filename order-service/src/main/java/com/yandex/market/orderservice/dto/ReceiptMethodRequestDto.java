package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.DeliveryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema
public record ReceiptMethodRequestDto(
        @NotNull(message = "Способ доставки должен быть указан")
        DeliveryMethod deliveryMethod,
        @NotNull(message = "Адрес доставки должен быть заполнен")
        AddressRequestDto address,
        @NotBlank(message = "Имя и фамилия получателя должны быть заполнены")
        @Size(min = 4, max = 201, message = "Длина имени получателя должна находиться в пределе от 4 до 201 символа")
        String receiverName,
        @NotBlank(message = "Телефон получателя должен быть заполнен")
        @Size(min = 11, max = 12, message = "Длина телефона должна составлять от 11 до 12 символов")
        String receiverPhone,
        @Email(message = "Почта получателя не соответствует стандарту")
        String receiverEmail,
        @NotNull(message = "Дата доставки должна быть заполнена")
        @FutureOrPresent(message = "Дата доставки должна быть не раньше сегодняшнего дня")
        LocalDate deliveryDate,
        @NotNull(message = "Время начала периода доставки должно быть заполнено")
        LocalTime deliveryStart,
        @NotNull(message = "Время конца периода доставки должно быть заполнено")
        LocalTime deliveryEnd,
        @PositiveOrZero(message = "Цена доставки не может быть отрицательной")
        double deliveryCost) {
}