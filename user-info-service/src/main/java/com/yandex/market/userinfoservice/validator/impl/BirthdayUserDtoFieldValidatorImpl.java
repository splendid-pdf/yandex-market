package com.yandex.market.userinfoservice.validator.impl;

import com.yandex.market.userinfoservice.validator.UserDtoFieldValidator;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BirthdayUserDtoFieldValidatorImpl implements UserDtoFieldValidator {
    @Override
    public void validate(UserRequestDto userRequestDto, List<String> exceptions) {
        if (StringUtils.isBlank(userRequestDto.getBirthday().toString())) {
            exceptions.add("Birthday can't be blank");
        } else if (userRequestDto.getBirthday().toString().matches("[12]\\\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\\\d|3[01])")) {
            exceptions.add("Incorrect format birthday");
        }
    }
}
