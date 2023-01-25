package com.yandex.market.userinfoservice.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yandex.market.userinfoservice.config.properties.ErrorInfoProperties;
import com.yandex.market.userinfoservice.model.Sex;
import com.yandex.market.userinfoservice.validator.enums.UserFilterFieldValidation;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openapitools.api.model.UserFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yandex.market.userinfoservice.utils.ValidationCodeConstants.INCORRECT_SEX_TYPE_ERROR_CODE;

@Component
@RequiredArgsConstructor
public class UserFilterValidator {

    private final ErrorInfoProperties properties;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void validate(@NotNull UserFilter userFilter) {
        List<String> exceptionMessages = new ArrayList<>();

        Arrays.stream(UserFilterFieldValidation.values())
                .forEach(field -> field.consume(this, userFilter, exceptionMessages));

        if (!CollectionUtils.isEmpty(exceptionMessages)) {
            throw new ValidationException(objectMapper.writeValueAsString(exceptionMessages));
        }
    }

    public void validateSex(@NotNull UserFilter userFilter, @NotNull List<String> exceptionMessages) {
        val sex = userFilter.getSex();
        if (StringUtils.isNotBlank(sex) && !EnumUtils.isValidEnumIgnoreCase(Sex.class, sex)) {
            exceptionMessages
                    .add(properties.getMessageByErrorCode(INCORRECT_SEX_TYPE_ERROR_CODE)
                            .formatted(sex));
        }
    }
}