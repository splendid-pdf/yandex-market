package com.yandex.market.shopservice.dto.branch;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ContactResponseDto(
        @Pattern(regexp = "^(\\+7|8)[\\s(]*\\d{3,4}[)\\s]*\\d{2,3}[\\s-]?\\d{2}[\\s-]?\\d{2}$",
                message = "Invalid \"Hotline phone number\" entered")
        @Size(min = 12, message = "\"Hotline phone number\" field should be don't less 12 characters long")
        String companyHotlinePhone,

        @Email(message = "\"Company Email\" field should be valid")
        @NotBlank(message = "\"Company Email\" field must not be empty")
        String companyEmail,

        @Pattern(regexp = "^(\\+7|8)[\\s(]*\\d{3,4}[)\\s]*\\d{2,3}[\\s-]?\\d{2}[\\s-]?\\d{2}$",
                message = "Invalid \"Hotline phone number\" entered")
        @Size(min = 12, message = "\"Hotline phone number\" field should be don't less 12 characters long")
        String branchHotlinePhone,

        @Pattern(regexp = "^(\\+7|8)[\\s(]*\\d{3,4}[)\\s]*\\d{2,3}[\\s-]?\\d{2}[\\s-]?\\d{2}$",
                message = "Invalid \"Service phone number\" entered")
        @Size(min = 12, message = "\"Service phone number\" field should be don't less 12 characters long")
        String branchServicePhone,

        @Email(message = "\"Email\" field should be valid")
        @NotBlank(message = "\"Email\" field must not be empty")
        String branchEmail) {
}