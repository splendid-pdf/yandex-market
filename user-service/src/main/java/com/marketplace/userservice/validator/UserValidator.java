package com.marketplace.userservice.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.userservice.validator.enums.UserFieldValidation;
import com.marketplace.userservice.config.properties.ErrorInfoProperties;
import com.marketplace.userservice.dto.request.UserRequestDto;
import com.marketplace.userservice.model.Sex;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.marketplace.userservice.utils.PatternConstants.*;
import static com.marketplace.userservice.utils.ValidationCodeConstants.*;

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
        val firstName = userRequestDto.firstName();
        val lastName = userRequestDto.lastName();

        if (StringUtils.isNotBlank(firstName) && !NAME_PATTERN.matcher(firstName).matches()) {
            exceptionMessages
                    .add(properties.getMessageByErrorCode(INCORRECT_FIRST_NAME_VALIDATION_ERROR_CODE)
                            .formatted(firstName));
        }

        if (StringUtils.isNotBlank(lastName) && !NAME_PATTERN.matcher(lastName).matches()) {
            exceptionMessages
                    .add(properties.getMessageByErrorCode(INCORRECT_LAST_NAME_VALIDATION_ERROR_CODE)
                            .formatted(lastName));
        }
    }

    public void validateEmail(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val email = userRequestDto.email();

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

    public void validatePhone(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val phone = userRequestDto.phone();

        if (StringUtils.isNotBlank(phone) && !PHONE_PATTERN.matcher(phone).matches()) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_PHONE_VALIDATION_ERROR_CODE)
                    .formatted(phone));
        }
    }

    public void validateSex(@NotNull UserRequestDto userRequestDto, @NotNull List<String> exceptionMessages) {
        val sex = userRequestDto.sex();
        if (StringUtils.isNotBlank(sex) && !EnumUtils.isValidEnumIgnoreCase(Sex.class, sex)) {
            exceptionMessages.add(properties.getMessageByErrorCode(INCORRECT_SEX_TYPE_ERROR_CODE)
                    .formatted(sex));
        }
    }
}