package com.yandex.market.userinfoservice.validator.impl;

import com.yandex.market.userinfoservice.validator.UserDtoFieldValidator;
import org.apache.commons.lang3.EnumUtils;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SexUserDtoFieldValidatorImpl implements UserDtoFieldValidator {
    @Override
    public void validate(UserRequestDto userRequestDto, List<String> exceptions) {
        //TODO: MUST DEBUG!!!
        //todo: does not work
        //todo: Output -> Resolved [org.springframework.http.converter.HttpMessageNotReadableException:
        //JSON parse error: Cannot construct instance of `org.openapitools.api.model.UserRequestDto$SexEnum`,
        //problem: Unexpected value 'x']
        if (!EnumUtils.isValidEnum(UserRequestDto.SexEnum.class, userRequestDto.getSex().toString())) {
            exceptions.add("Incorrect sex");
        }
    }
}
