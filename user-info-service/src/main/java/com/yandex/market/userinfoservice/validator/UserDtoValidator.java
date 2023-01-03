package com.yandex.market.userinfoservice.validator;

import jakarta.validation.ValidationException;
import org.jetbrains.annotations.NotNull;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public record UserDtoValidator(List<FieldValidator<UserRequestDto>> list) {

    public void validateDto(@NotNull UserRequestDto userRequestDto) {
        List<String> exceptionMessages = new ArrayList<>();

        list.forEach(fieldsDto -> fieldsDto.validate(userRequestDto, exceptionMessages));

        if (!CollectionUtils.isEmpty(exceptionMessages)) {
            throw new ValidationException(String.join(", ", exceptionMessages));
        }
    }

}
