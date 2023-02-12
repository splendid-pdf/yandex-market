package com.yandex.market.orderservice.validator;

import com.yandex.market.orderservice.validator.annotation.ReceiverNameConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ReceiverNameConstraintValidator implements ConstraintValidator<ReceiverNameConstraint, String> {
    private static final Pattern REGEX_PATTERN_RECEIVER_NAME = Pattern
            .compile("^[а-яА-Яa-zA-Z-]+$");
    @Override
    public void initialize(ReceiverNameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String receiverName, ConstraintValidatorContext context) {
        return doReceiverNameFormatCheck(receiverName);
    }

    private boolean doReceiverNameFormatCheck(String receiverName) {
        return REGEX_PATTERN_RECEIVER_NAME
                .matcher(receiverName)
                .matches();
    }
}