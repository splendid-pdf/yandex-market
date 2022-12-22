package com.yandex.market.shopservice.dto.branch;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactDto(
        @Size(min = 6, max = 18, message = "Number should be between 6 and 18") String hotlinePhone,
        @Size(min = 6, max = 18, message = "Number should be between 6 and 18") String servicePhone,
        @NotBlank(message = "Field \"Email\" must not be empty")
        @Email(message = "Field \"Email\" should be valid") String email) {
}
