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

import static com.yandex.market.userinfoservice.utils.Constants.BLANK_EMAIL_CODE;
import static com.yandex.market.userinfoservice.utils.Constants.INCORRECT_EMAIL_CODE;

@Component
@RequiredArgsConstructor
public class EmailUserDtoFieldValidatorImpl implements FieldValidator<UserRequestDto> {
    private final ErrorInfoProperties properties;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    @Override
    public void validate(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val email = userRequestDto.getEmail();

        if (StringUtils.isBlank(email)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_EMAIL_CODE));
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_EMAIL_CODE).formatted(email));
        }
    }
}
