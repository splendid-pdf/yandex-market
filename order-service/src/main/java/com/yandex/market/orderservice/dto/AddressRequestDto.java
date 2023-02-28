package com.yandex.market.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema
public record AddressRequestDto(
        @NotBlank(message = "Страна получателя должна быть заполнена")
        String country,
        @NotBlank(message = "Регион получателя должен быть заполнен")
        String region,
        @NotBlank(message = "Город получателя должен быть заполнен")
        String city,
        @Size(max = 6, min = 6, message = "Длина почтового кода должна состовлять ровно 6 цифр. postCode - ${postCode}")
        @NotBlank(message = "Почтовый индекс должен быть заполнен")
        String postCode,
        @NotBlank(message = "Улица получателя должна быть заполнена")
        String street,
        @NotBlank(message = "Номер дома должен быть заполнен")
        String houseNumber,
        @NotBlank(message = "Номер квартиры должен быть заполнен")
        String officeNumber) {
}