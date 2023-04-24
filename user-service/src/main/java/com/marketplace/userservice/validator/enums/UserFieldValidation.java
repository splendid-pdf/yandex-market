package com.marketplace.userservice.validator.enums;

import com.marketplace.userservice.dto.request.UserRequestDto;
import com.marketplace.userservice.validator.UserValidator;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;

public enum UserFieldValidation {
    EMAIL(UserValidator::validateEmail),
    NAME(UserValidator::validateName),
    PHONE(UserValidator::validatePhone),
    SEX(UserValidator::validateSex);
    private final TriConsumer<UserValidator, UserRequestDto, List<String>> consumer;

    UserFieldValidation(TriConsumer<UserValidator, UserRequestDto, List<String>> consumer) {
        this.consumer = consumer;
    }

    public void consume(UserValidator validator, UserRequestDto requestDto, List<String> messages) {
        consumer.accept(validator, requestDto, messages);
    }
}