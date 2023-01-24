package com.yandex.market.userinfoservice.validator.enums;

import com.yandex.market.userinfoservice.validator.UserFilterValidator;
import org.apache.logging.log4j.util.TriConsumer;
import org.openapitools.api.model.UserFilter;

import java.util.List;

public enum UserFilterFieldValidation {
    SEX(UserFilterValidator::validateSex);

    private final TriConsumer<UserFilterValidator, UserFilter, List<String>> consumer;

    UserFilterFieldValidation(TriConsumer<UserFilterValidator, UserFilter, List<String>> consumer) {
        this.consumer = consumer;
    }

    public void consume(UserFilterValidator validator, UserFilter userFilter, List<String> messages) {
        consumer.accept(validator, userFilter, messages);
    }
}