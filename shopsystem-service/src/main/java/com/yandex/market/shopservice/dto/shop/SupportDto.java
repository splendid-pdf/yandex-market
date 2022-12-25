package com.yandex.market.shopservice.dto.shop;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SupportDto(
        @Pattern(regexp = "^(\\+7|8)[\\s(]*\\d{3,4}[)\\s]*\\d{2,3}[\\s-]?\\d{2}[\\s-]?\\d{2}$",
                message = "Invalid \"Support number\" entered")
        @Size(min = 12, message = "\"Support number\" field should be don't less 12 characters long")
        String number,

        @Email(message = "\"Email\" field should be valid")
        @NotBlank(message = "\"Email\" field must not be empty")
        String email) {
}
