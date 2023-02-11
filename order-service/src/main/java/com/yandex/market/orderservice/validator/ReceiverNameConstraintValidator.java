package com.yandex.market.orderservice.validator;

import com.yandex.market.orderservice.validator.annotation.ReceiverNameConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ReceiverNameConstraintValidator implements ConstraintValidator<ReceiverNameConstraint, String> {

    @Override
    public void initialize(ReceiverNameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String receiverName, ConstraintValidatorContext context) {
        return doReceiverNameFormatCheck(receiverName);
    }

    private boolean doReceiverNameFormatCheck(String receiverName) {
        String regexPatternReceiverName = "^[а-яА-Яa-zA-Z-]+$";

        return Pattern.compile(regexPatternReceiverName)
                .matcher(receiverName)
                .matches();
    }
}