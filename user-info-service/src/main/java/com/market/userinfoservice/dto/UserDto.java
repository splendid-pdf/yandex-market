package com.market.userinfoservice.dto;

import com.market.userinfoservice.model.Sex;

import java.time.LocalDate;

public record UserDto(
        String firstName,
        String middleName,
        String lastName,
        String phone,
        String email,
        String login,
        String password,
        LocalDate birthday,
        Sex sex
) {

}
