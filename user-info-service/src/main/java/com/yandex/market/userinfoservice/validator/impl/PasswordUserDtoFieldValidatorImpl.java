package com.yandex.market.userinfoservice.validator.impl;

import com.yandex.market.userinfoservice.validator.UserDtoFieldValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordUserDtoFieldValidatorImpl implements UserDtoFieldValidator {
    @Override
    public void validate(UserRequestDto userRequestDto, List<String> exceptions) {

        if (StringUtils.isBlank(userRequestDto.getPassword())) {
            exceptions.add("Password can't be blank");
        }
    }
}
