package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.DeliveryMethod;
import com.yandex.market.orderservice.validator.annotation.ReceiverEmailConstraint;
import com.yandex.market.orderservice.validator.annotation.ReceiverNameConstraint;
import com.yandex.market.orderservice.validator.annotation.ReceiverPhoneConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema
public record ReceiptMethodRequest(
        @NotNull(message = "Способ доставки должен быть указан")
        DeliveryMethod deliveryMethod,

        @NotNull(message = "Адрес доставки должен быть заполнен")
        @Valid
        AddressRequest address,

        @NotBlank(message = "Имя получателя должно быть заполнено")
        @Size(min = 4, max = 201, message = "Длина имени получателя должна находиться в пределе от 4 до 201 символа")
        @ReceiverNameConstraint
        String receiverFirstName,

        @NotBlank(message = "Фамилия получателя должно быть заполнено")
        @Size(min = 4, max = 201, message = "Длина имени получателя должна находиться в пределе от 4 до 201 символа")
        @ReceiverNameConstraint
        String receiverLastName,

        @NotBlank(message = "Телефон получателя должен быть заполнен")
        @Size(min = 11, max = 12, message = "Длина телефона должна составлять от 11 до 12 символов")
        @ReceiverPhoneConstraint
        String receiverPhone,
        @NotBlank
        @Email(message = "Введённый адресс электронной почты является некорректным")
        @ReceiverEmailConstraint
        String receiverEmail,
        @NotNull(message = "Дата доставки должна быть заполнена")
        @FutureOrPresent(message = "Дата доставки должна быть не раньше сегодняшнего дня")
        LocalDate deliveryDate,
//        @NotNull(message = "Время начала периода доставки должно быть заполнено")
//        LocalTime deliveryStart,
//        @NotNull(message = "Время конца периода доставки должно быть заполнено")
//        LocalTime deliveryEnd,
        @PositiveOrZero(message = "Цена доставки не может быть отрицательной")
        Long deliveryCost) {
}