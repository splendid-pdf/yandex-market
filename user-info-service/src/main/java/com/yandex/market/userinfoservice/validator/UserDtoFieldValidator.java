package com.yandex.market.userinfoservice.validator;

import org.openapitools.api.model.UserRequestDto;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface UserDtoFieldValidator {
    void validate(UserRequestDto userRequestDto, List<String> exceptions);
}
