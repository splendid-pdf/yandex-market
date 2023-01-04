package com.yandex.market.userinfoservice.validator.impl;

import com.yandex.market.userinfoservice.config.properties.ErrorInfoProperties;
import com.yandex.market.userinfoservice.validator.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.yandex.market.userinfoservice.utils.Constants.BLANK_BIRTHDAY_CODE;

@Component
@RequiredArgsConstructor
public class BirthdayUserDtoFieldValidatorImpl implements FieldValidator<UserRequestDto> {
    private final ErrorInfoProperties properties;

    @Override
    public void validate(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        if (userRequestDto.getBirthday() == null) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_BIRTHDAY_CODE));
        }


    }
}

