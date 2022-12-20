package com.yandex.market.userinfoservice.dto;

import com.yandex.market.userinfoservice.model.Sex;
import com.yandex.market.userinfoservice.validate.EnumValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserDto(

        @NotBlank(message = "Field \"FirstName\" must not be empty")
        String firstName,

        String middleName,

        @NotBlank(message = "Field \"LastName\" must not be empty")
        String lastName,

        @NotBlank(message = "Field \"Phone\" must not be empty")
        String phone,

        @NotBlank(message = "Field \"Email\" must not be empty")
        @Email(message = "Wrong mail format")
        String email,

        @NotBlank(message = "Field \"Login\" must not be empty")
        String login,

        @NotBlank(message = "Field \"Password\" must not be empty")
        String password,


        @Pattern(regexp = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))",
                message = "Invalid format date")
        String birthday,

        @EnumValidator(
                enumClazz = Sex.class,
                message = "Invalid data",
                groups = {Sex.class}
        )
        String sex
) {

}
