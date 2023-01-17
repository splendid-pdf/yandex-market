package com.yandex.market.userinfoservice.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.userinfoservice.config.properties.ErrorInfoProperties;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.yandex.market.userinfoservice.utils.PatternConstants.*;
import static com.yandex.market.userinfoservice.utils.ValidationCodeConstants.*;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final ErrorInfoProperties properties;
    private final ObjectMapper objectMapper;


    @SneakyThrows
    public void validate(@NotNull UserRequestDto userRequestDto) {
        List<String> exceptionMessages = new ArrayList<>();

        Arrays.stream(UserFieldValidation.values())
                .forEach(field -> field.consume(this, userRequestDto, exceptionMessages));

        if (!CollectionUtils.isEmpty(exceptionMessages)) {
            throw new ValidationException(objectMapper.writeValueAsString(exceptionMessages));
        }
    }

    public void validateName(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val firstName = userRequestDto.getFirstName();
        val lastName = userRequestDto.getLastName();

        if (StringUtils.isBlank(firstName)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_FIRST_NAME_VALIDATION_ERROR_CODE));
        } else if (!NAME_PATTERN.matcher(firstName).matches()) {
            exceptionMessages
                    .add(properties.getMessageByErrorCode(INCORRECT_FIRST_NAME_VALIDATION_ERROR_CODE)
                            .formatted(firstName));
        }

        if (StringUtils.isBlank(lastName)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_LAST_NAME_VALIDATION_ERROR_CODE));
        } else if (!NAME_PATTERN.matcher(lastName).matches()) {
            exceptionMessages
                    .add(properties.getMessageByErrorCode(INCORRECT_LAST_NAME_VALIDATION_ERROR_CODE)
                            .formatted(lastName));
        }
    }

    public void validateEmail(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val email = userRequestDto.getEmail();

        if (StringUtils.isBlank(email)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_EMAIL_VALIDATION_ERROR_CODE));
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            exceptionMessages
                    .add(properties.getMessageByErrorCode(INCORRECT_EMAIL_VALIDATION_ERROR_CODE)
                            .formatted(email));
        }
    }

    public void validatePassword(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val password = userRequestDto.getPassword();

        if (StringUtils.isBlank(password)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_PASSWORD_VALIDATION_ERROR_CODE));
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            exceptionMessages
                    .add(properties.getMessageByErrorCode(INCORRECT_PASSWORD_VALIDATION_ERROR_CODE)
                            .formatted(password));
        }
    }

    public void validateBirthday(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val birthday = userRequestDto.getBirthday();

        if (Objects.isNull(birthday)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_BIRTHDAY_VALIDATION_ERROR_CODE));
        }
    }

    public void validatePhone(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val phone = userRequestDto.getPhone();

        if (StringUtils.isBlank(phone)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_PHONE_VALIDATION_ERROR_CODE));
        } else if (!REG_VALID_PHONE.matcher(phone).matches()) {
            exceptionMessages
                    .add(properties.getMessageByErrorCode(INCORRECT_PHONE_VALIDATION_ERROR_CODE)
                            .formatted(phone));
        }
    }
}