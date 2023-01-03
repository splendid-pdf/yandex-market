package com.yandex.market.userinfoservice.validator.impl;

import com.yandex.market.userinfoservice.config.properties.ErrorInfoProperties;
import com.yandex.market.userinfoservice.validator.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.yandex.market.userinfoservice.utils.Constants.NULL_SEX_CODE;

@Component
@RequiredArgsConstructor
public class SexUserDtoFieldValidatorImpl implements FieldValidator<UserRequestDto> {
    private final ErrorInfoProperties properties;

    @Override
    public void validate(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        if (userRequestDto.getSex() == null) {
            exceptionMessages.add(properties.getMessageByErrorCode(NULL_SEX_CODE));
        }
    }
}
