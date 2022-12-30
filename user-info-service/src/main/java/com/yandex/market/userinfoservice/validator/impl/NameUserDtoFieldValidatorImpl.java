package com.yandex.market.userinfoservice.validator.impl;

import com.yandex.market.userinfoservice.validator.UserDtoFieldValidator;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class NameUserDtoFieldValidatorImpl implements UserDtoFieldValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]{2,100}");
    @Override
    public void validate(UserRequestDto userRequestDto, List<String> exceptions) {
        //TODO: change duplicate
        if (StringUtils.isBlank(userRequestDto.getFirstName())) {
            exceptions.add("Firstname can't be blank");
        } else if (!NAME_PATTERN.matcher(userRequestDto.getFirstName()).matches()) {
            exceptions.add("Incorrect format firstname");
        }

        if (StringUtils.isBlank(userRequestDto.getLastName())) {
            exceptions.add("Lastname can't be blank");
        } else if (!NAME_PATTERN.matcher(userRequestDto.getLastName()).matches()) {
            exceptions.add("Incorrect format lastname");
        }
    }
}
