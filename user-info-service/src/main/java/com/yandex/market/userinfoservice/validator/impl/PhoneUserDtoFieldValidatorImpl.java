package com.yandex.market.userinfoservice.validator.impl;

import com.yandex.market.userinfoservice.config.properties.ErrorInfoProperties;
import com.yandex.market.userinfoservice.validator.FieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

import static com.yandex.market.userinfoservice.utils.Constants.BLANK_PHONE_CODE;

@Component
@RequiredArgsConstructor
public class PhoneUserDtoFieldValidatorImpl implements FieldValidator<UserRequestDto> {

    private static final Pattern REG_VALID_PHONE = Pattern.compile("([7-8])?(9)(\\d){9}");
    private final ErrorInfoProperties properties;

    @Override
    public void validate(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val phone = userRequestDto.getPhone();

        if (StringUtils.isBlank(phone)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_PHONE_CODE));
            return;
        }

//        String phoneNumber = phone.replaceAll("\\D", "");
//        if (!phoneNumber.matches(REG_VALID_PHONE)) {
//            exceptionMessages.add("Incorrect format for phone number");
//        }


    }


}
