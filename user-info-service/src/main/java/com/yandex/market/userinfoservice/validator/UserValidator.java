package com.yandex.market.userinfoservice.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.userinfoservice.config.properties.ErrorInfoProperties;
import com.yandex.market.userinfoservice.model.Sex;
import com.yandex.market.userinfoservice.model.SocialNetwork;
import com.yandex.market.userinfoservice.validator.enums.UserFieldValidation;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        val middleName = userRequestDto.getMiddleName();

        if (StringUtils.isBlank(firstName)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_FIRST_NAME_VALIDATION_ERROR_CODE));
        } else if (!NAME_PATTERN.matcher(firstName).matches()) {
            exceptionMessages
                    .add(properties.getMessageByErrorCode(INCORRECT_FIRST_NAME_VALIDATION_ERROR_CODE)
                            .formatted(firstName));
        }

        if (!NAME_PATTERN.matcher(middleName).matches()) {
            exceptionMessages
                    .add(properties.getMessageByErrorCode(INCORRECT_MIDDLE_NAME_VALIDATION_ERROR_CODE)
                            .formatted(middleName));
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

//        if (userRepository.existsByEmail(email)) {
//            throw new IllegalArgumentException(USER_WITH_THE_SAME_EMAIL_IS_EXISTS_MESSAGE.formatted(email));
//        }
        if (StringUtils.isBlank(email)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_EMAIL_VALIDATION_ERROR_CODE));
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_EMAIL_VALIDATION_ERROR_CODE)
                    .formatted(email));
        }
    }

    public void validatePassword(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val password = userRequestDto.getPassword();

        if (StringUtils.isBlank(password)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_PASSWORD_VALIDATION_ERROR_CODE));
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_PASSWORD_VALIDATION_ERROR_CODE)
                    .formatted(password));
        }
    }

    public void validatePhone(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val phone = userRequestDto.getPhone();

        if (StringUtils.isBlank(phone)) {
            exceptionMessages.add(properties.getMessageByErrorCode(BLANK_PHONE_VALIDATION_ERROR_CODE));
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_PHONE_VALIDATION_ERROR_CODE)
                    .formatted(phone));
        }
    }

    public void validateSex(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val sex = userRequestDto.getSex();
        if (sex != null && !EnumUtils.isValidEnumIgnoreCase(Sex.class, sex)) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_SEX_TYPE_ERROR_CODE)
                    .formatted(sex));
        }
    }

    public void validateContact(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        if (!CollectionUtils.isEmpty(userRequestDto.getContacts())) {
            userRequestDto.getContacts().forEach(contactDto -> {
                val networkValue = contactDto.getValue();
                val socialNetwork = contactDto.getType();

                if (!EnumUtils.isValidEnum(SocialNetwork.class, socialNetwork) && socialNetwork != null) {
                    exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_SOCIAL_NETWORK_ERROR_CODE)
                            .formatted(socialNetwork));
                    if (StringUtils.isBlank(networkValue) && networkValue != null) {
                        exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_SOCIAL_NETWORK_VALUE_ERROR_CODE)
                                .formatted(networkValue));
                    }
                } else if (StringUtils.isBlank(networkValue) && socialNetwork != null) {
                    exceptionMessages.add(properties.getMessageByErrorCode(BLANK_SOCIAL_NETWORK_VALUE_ERROR_CODE));
                } else if (networkValue != null && StringUtils.isBlank(socialNetwork)) {
                    exceptionMessages.add(properties.getMessageByErrorCode(BLANK_SOCIAL_NETWORK_ERROR_CODE));
                }
            });
        }
    }
}