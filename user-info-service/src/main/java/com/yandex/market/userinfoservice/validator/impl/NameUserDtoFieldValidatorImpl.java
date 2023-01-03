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

import static com.yandex.market.userinfoservice.utils.Constants.*;

@Component
@RequiredArgsConstructor
public class NameUserDtoFieldValidatorImpl implements FieldValidator<UserRequestDto> {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]{2,100}");
    private final ErrorInfoProperties properties;

    @Override
    public void validate(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val firstName = userRequestDto.getFirstName();

        if (StringUtils.isBlank(firstName)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_FIRST_NAME_CODE));
        } else if (!NAME_PATTERN.matcher(firstName).matches()) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_FIRST_NAME_CODE).formatted(firstName));
        }

        if (StringUtils.isBlank(userRequestDto.getLastName())) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_LAST_NAME_CODE));
        } else if (!NAME_PATTERN.matcher(userRequestDto.getLastName()).matches()) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_LAST_NAME_CODE).formatted());
        }
    }
}
