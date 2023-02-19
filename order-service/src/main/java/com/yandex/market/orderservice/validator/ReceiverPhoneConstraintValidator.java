package com.yandex.market.orderservice.validator;

import com.yandex.market.orderservice.validator.annotation.ReceiverPhoneConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ReceiverPhoneConstraintValidator implements ConstraintValidator<ReceiverPhoneConstraint, String> {
    private static final Pattern REGEX_PATTERN_RECEIVER_PHONE = Pattern
            .compile("^[/+8][0-9]+$");

    @Override
    public void initialize(ReceiverPhoneConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String receiverPhone, ConstraintValidatorContext context) {
        return doReceiverPhoneFormatCheck(receiverPhone);
    }

    private boolean doReceiverPhoneFormatCheck(String receiverPhone) {
        return REGEX_PATTERN_RECEIVER_PHONE
                .matcher(receiverPhone)
                .matches();
    }
}