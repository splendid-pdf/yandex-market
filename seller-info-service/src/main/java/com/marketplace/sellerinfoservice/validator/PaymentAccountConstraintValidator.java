package com.marketplace.sellerinfoservice.validator;

import com.marketplace.sellerinfoservice.validator.annotation.PaymentAccountConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PaymentAccountConstraintValidator implements ConstraintValidator<PaymentAccountConstraint, String> {

    private static final Pattern REGEX_PATTERN_PAYMENT_ACCOUNT = Pattern
            .compile("^[0-9]{20}$");

    @Override
    public void initialize(PaymentAccountConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String paymentAccount, ConstraintValidatorContext context) {
        return doPaymentAccountFormatCheck(paymentAccount);
    }

    private boolean doPaymentAccountFormatCheck(String paymentAccount) {
        if (paymentAccount == null || paymentAccount.isEmpty()) {
            return true;
        }
        return REGEX_PATTERN_PAYMENT_ACCOUNT
                .matcher(paymentAccount)
                .matches();
    }
}