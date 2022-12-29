package com.yandex.market.userinfoservice.validate;

import jakarta.validation.ValidationException;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class Validator {

    public void validateDto(UserRequestDto userRequestDto) {
        List<String> exceptions = new ArrayList<>();

        if (StringUtils.isBlank(userRequestDto.getFirstName())) {
            exceptions.add("Firstname can't be blank");
        } else if (!userRequestDto.getFirstName().matches("[a-zA-Z]{2,50}")) {
            exceptions.add("Incorrect format firstname");
        }

        if (StringUtils.isBlank(userRequestDto.getLastName())) {
            exceptions.add("Lastname can't be blank");
        } else if (!userRequestDto.getLastName().matches("[a-zA-Z]{2,100}")) {
            exceptions.add("Incorrect format lastname");
        }

        if (StringUtils.isBlank(userRequestDto.getPhone())) {
            exceptions.add("Phone number can't be blank");
        } else if (!userRequestDto.getPhone()
                .matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")) {
            exceptions.add("Incorrect format phone number");
        }

        if (StringUtils.isBlank(userRequestDto.getEmail())) {
            exceptions.add("Email can't be blank");
        } else if (!userRequestDto.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            exceptions.add("Incorrect format email");
        }

        if (StringUtils.isBlank(userRequestDto.getPassword())) {
            exceptions.add("Password can't be blank");
        }

        if (StringUtils.isBlank(userRequestDto.getBirthday().toString())) {
            exceptions.add("Birthday can't be blank");
        } else if (userRequestDto.getBirthday().toString().matches("[12]\\\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\\\d|3[01])")) {
            exceptions.add("Incorrect format birthday");
        }

        if (!EnumUtils.isValidEnum(UserRequestDto.SexEnum.class, userRequestDto.getSex().toString())) {
            exceptions.add("Incorrect sex");
        }

        if (!CollectionUtils.isEmpty(exceptions)) {
            throw new ValidationException(String.join(", ", exceptions));
        }
    }
}
