package com.yandex.market.productservice.dto.request;

import com.yandex.market.productservice.model.ValueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductCharacteristicRequest(

        @Size(min = 3, max = 50, message = "The name of the characteristic must be in the range from 3 to 50 characters")
        String name,

        @NotBlank(message = "The value of the characteristic must be specified")
        String value,

        @NotNull(message = "The value type cannot be null" )
        ValueType valueType,

        String groupCharacteristic

) {
}
