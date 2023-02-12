package com.yandex.market.orderservice.validator;

import com.yandex.market.orderservice.validator.annotation.ReceiverEmailConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ReceiverEmailConstraintValidator implements ConstraintValidator<ReceiverEmailConstraint, String> {
    private static final Pattern REGEX_PATTERN_RECEIVER_EMAIL = Pattern
            .compile("^(?=.{1,50}@)[a-zA-Z0-9_!#$%&'+=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    @Override
    public void initialize(ReceiverEmailConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String receiverEmail, ConstraintValidatorContext context) {
        return doReceiverEmailFormatCheck(receiverEmail);
    }

    private boolean doReceiverEmailFormatCheck(String receiverEmail) {
        return REGEX_PATTERN_RECEIVER_EMAIL
                .matcher(receiverEmail)
                .matches();
    }
}