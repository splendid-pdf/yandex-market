package com.yandex.market.userinfoservice.validator;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public record UserDtoValidator(List<UserDtoFieldValidator> list) {
    public void validateDto(UserRequestDto userRequestDto) {
        List<String> exceptions = new ArrayList<>();

        list.forEach(fieldsDto -> fieldsDto.validate(userRequestDto, exceptions));

        if (!CollectionUtils.isEmpty(exceptions)) {
            throw new ValidationException(String.join(", ", exceptions));
        }
    }
}
