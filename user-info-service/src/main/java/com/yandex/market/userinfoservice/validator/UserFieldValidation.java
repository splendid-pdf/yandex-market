package com.yandex.market.userinfoservice.validator;

import org.apache.logging.log4j.util.TriConsumer;
import org.openapitools.api.model.UserRequestDto;

import java.util.List;

public enum UserFieldValidation {
    BIRTHDAY(UserValidator::validateBirthday),
    EMAIL(UserValidator::validateEmail),
    NAME(UserValidator::validateName),
    PASSWORD(UserValidator::validatePassword),
    PHONE(UserValidator::validatePhone);

    private final TriConsumer<UserValidator, UserRequestDto, List<String>> consumer;

    UserFieldValidation(TriConsumer<UserValidator, UserRequestDto, List<String>> consumer) {
        this.consumer = consumer;
    }

    public void consume(UserValidator validator, UserRequestDto requestDto, List<String> messages) {
        consumer.accept(validator, requestDto, messages);
    }
}