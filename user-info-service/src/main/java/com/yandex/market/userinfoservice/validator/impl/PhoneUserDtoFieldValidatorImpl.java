package com.yandex.market.userinfoservice.validator.impl;

import com.yandex.market.userinfoservice.validator.UserDtoFieldValidator;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PhoneUserDtoFieldValidatorImpl implements UserDtoFieldValidator {

    private static final String REG_VALID_PHONE = "([7-8])?(9)(\\d){9}";

    @Override
    public void validate(UserRequestDto userRequestDto, List<String> exceptions) {
        //todo: string to builder

        if (StringUtils.isBlank(userRequestDto.getPhone())) {
            exceptions.add("Phone number can't be blank");
            return;
        }
        String phoneNumber = userRequestDto.getPhone().replaceAll("\\D", "");
        if (!phoneNumber.matches(REG_VALID_PHONE)) {
            exceptions.add("Incorrect format for number");
        }


    }


}
