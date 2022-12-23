package com.yandex.market.shopservice.dto.branch;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactDto(
        @Size(min = 12, max = 12,
                message = "\"Hotline phone number\" field should be 12 characters long")
        String hotlinePhone,

        @Size(min = 12, max = 12,
                message = "\"Service phone number\" field should be 12 characters long")
        String servicePhone,

        @Email(message = "\"Email\" field should be valid")
        @NotBlank(message = "\"Email\" field must not be empty")
        String email) {
}
