package com.yandex.market.productservice.dto;

import com.yandex.market.productservice.model.Dimensions;
import com.yandex.market.productservice.model.ProductType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;


public record ProductRequestDto(
        @NotBlank(message = "Наименование товара должно быть указано")
        @Size(min = 3, max = 30, message = "Наименование товара должно быть в интервале от 3 до 30 символов")
        String name,
        @Max(value = 200, message = "Описание товара должно быть в интервале до 200 символов")
        String description,
        @NotBlank(message = "Тип товара должен быть указан")
        ProductType productType,
        @NotBlank(message = "Производитель товара должен быть указан")
        @Size(min = 3, max = 30, message = "Название производителя должно быть в интервале от 3 до 30 символов")
        String manufacturer,
        @NotBlank(message = "Ссылка на изображение не может быть пустой")
        String imageUrl,
        @PositiveOrZero(message = "Вес не может быть отрицательным")
        Double weight,
        List<@Valid CharacteristicDto> characteristics,
        @Valid
        Dimensions dimensions,
        Boolean isVisible

        //todo: features хз что это у нас в модели этого нет, но на миро есть, надо разобраться

) {

}
