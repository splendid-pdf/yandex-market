package com.yandex.market.orderservice.validator;

import com.yandex.market.orderservice.validator.annotation.ReceiverPhoneConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ReceiverPhoneConstraintValidator implements ConstraintValidator<ReceiverPhoneConstraint, String> {

    @Override
    public void initialize(ReceiverPhoneConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String receiverPhone, ConstraintValidatorContext context) {
        return doReceiverPhoneFormatCheck(receiverPhone);
    }

    private boolean doReceiverPhoneFormatCheck(String receiverPhone) {
        String regexPatternReceiverPhone = "^[0-9+]+$";

        return Pattern.compile(regexPatternReceiverPhone)
                .matcher(receiverPhone)
                .matches();
    }
}