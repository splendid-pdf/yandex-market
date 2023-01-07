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

import static com.yandex.market.userinfoservice.utils.PatternConstants.EMAIL_PATTERN;
import static com.yandex.market.userinfoservice.utils.PatternConstants.NAME_PATTERN;
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

    public void validateBirthday(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        if (userRequestDto.getBirthday() == null) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_BIRTHDAY_CODE));
        }
    }

    public void validateEmail(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val email = userRequestDto.getEmail();

        if (StringUtils.isBlank(email)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_EMAIL_CODE));
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_EMAIL_CODE).formatted(email));
        }
    }

    public void validateFirstName(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
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

    public void validatePassword(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        if (StringUtils.isBlank(userRequestDto.getPassword())) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_PASSWORD_CODE));
        }
    }

    public void validatePhone(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
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

    public void validateSex(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        if (userRequestDto.getSex() == null) {
            exceptionMessages.add(properties.getMessageByErrorCode(NULL_SEX_CODE));
        }
    }

}
