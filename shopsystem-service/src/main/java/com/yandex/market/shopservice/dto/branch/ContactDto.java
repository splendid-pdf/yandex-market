package com.yandex.market.shopservice.dto.branch;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ContactDto(
        @Pattern(regexp = "^[+7]\\d{11}$", message = "Invalid \"Hotline phone number\" entered")
        @Size(min = 12, max = 12, message = "\"Hotline phone number\" field should be 12 characters long")
        String hotlinePhone,

        @Pattern(regexp = "^[+7]\\d{11}$", message = "Invalid \"Service phone number\" entered")
        @Size(min = 12, max = 12, message = "\"Service phone number\" field should be 12 characters long")
        String servicePhone,

        @Email(message = "\"Email\" field should be valid")
        @NotBlank(message = "\"Email\" field must not be empty")
        String email) {
}
