package com.market.userinfoservice.dto;

import com.market.userinfoservice.model.Sex;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserDto(
        @NotNull String firstName,
        @NotNull String middleName,
        @NotNull String lastName,
        @NotNull String phone,
        @NotNull String email,
        @NotNull String login,
        @NotNull String password,
        @NotNull LocalDate birthday,
        @NotNull Sex sex
) {

}
