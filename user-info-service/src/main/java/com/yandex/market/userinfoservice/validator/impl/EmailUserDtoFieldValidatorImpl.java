package com.yandex.market.userinfoservice.validator.impl;

import com.yandex.market.userinfoservice.validator.UserDtoFieldValidator;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailUserDtoFieldValidatorImpl implements UserDtoFieldValidator {
    @Override
    public void validate(UserRequestDto userRequestDto, List<String> exceptions) {
        if (StringUtils.isBlank(userRequestDto.getEmail())) {
            exceptions.add("Email can't be blank");
        } else if (!userRequestDto.getEmail().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            exceptions.add("Incorrect format email");
        }
    }
}
