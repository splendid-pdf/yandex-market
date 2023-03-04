package com.yandex.market.userservice.validator;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.userservice.config.properties.ErrorInfoProperties;
import com.yandex.market.userservice.dto.request.UserRegistrationDto;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.yandex.market.userservice.utils.PatternConstants.EMAIL_PATTERN;
import static com.yandex.market.userservice.utils.PatternConstants.PASSWORD_PATTERN;
import static com.yandex.market.userservice.utils.ValidationCodeConstants.*;

@Component
@RequiredArgsConstructor
public class UserRegistrationValidator {

    private final ErrorInfoProperties properties;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void validate(UserRegistrationDto userRegistrationDto) {
        List<String> exceptionMessages = new ArrayList<>();

        val password = userRegistrationDto.password();

        if (StringUtils.isBlank(password)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_PASSWORD_VALIDATION_ERROR_CODE));
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_PASSWORD_VALIDATION_ERROR_CODE)
                    .formatted(password));
        }

        val email = userRegistrationDto.email();

        if (StringUtils.isBlank(email)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_EMAIL_VALIDATION_ERROR_CODE));
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_EMAIL_VALIDATION_ERROR_CODE)
                    .formatted(email));
        }

        if (!CollectionUtils.isEmpty(exceptionMessages)) {
            throw new ValidationException(objectMapper.writeValueAsString(exceptionMessages));
        }
    }


}
