package com.yandex.market.shopservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SupportDto(@Size(min = 6, max = 18) String number,
                         @NotBlank(message = "Field \"Email\" must not be empty")
                         @Email(message = "Field \"Email\" should be valid") String email) {
}
