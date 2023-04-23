package com.yandex.market.sellerinfoservice.validator;

import com.yandex.market.sellerinfoservice.validator.annotation.SellerEmailConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SellerEmailConstraintValidator implements ConstraintValidator<SellerEmailConstraint, String> {

    private static final Pattern REGEX_PATTERN_SELLER_EMAIL = Pattern
            .compile("^(?=.{1,50}@)[a-zA-Z0-9_!#$%&'+=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    @Override
    public void initialize(SellerEmailConstraint constraintAnnotation) {
    }


    @Override
    public boolean isValid(String sellerEmail, ConstraintValidatorContext context) {
        return doSellerEmailFormatCheck(sellerEmail);
    }

    private boolean doSellerEmailFormatCheck(String sellerEmail) {
        return REGEX_PATTERN_SELLER_EMAIL
                .matcher(sellerEmail)
                .matches();
    }
}