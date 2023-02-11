package com.yandex.market.orderservice.validator;

import com.yandex.market.orderservice.validator.annotation.ReceiverEmailConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ReceiverEmailConstraintValidator implements ConstraintValidator<ReceiverEmailConstraint, String> {

    @Override
    public void initialize(ReceiverEmailConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String receiverEmail, ConstraintValidatorContext context) {
        return doReceiverEmailFormatCheck(receiverEmail);
    }

    private boolean doReceiverEmailFormatCheck(String receiverEmail) {
        String regexPatternReceiverEmail = "^(?=.{1,50}@)[a-zA-Z0-9_!#$%&'+=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        return Pattern.compile(regexPatternReceiverEmail)
                .matcher(receiverEmail)
                .matches();
    }
}