package com.marketplace.sellerinfoservice.validator;

import com.marketplace.sellerinfoservice.validator.annotation.SellerPasswordConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SellerPasswordConstraintValidator implements ConstraintValidator<SellerPasswordConstraint, String> {

    private static final Pattern REGEX_PATTERN_SELLER_PASSWORD = Pattern
            .compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,30}$");

    @Override
    public void initialize(SellerPasswordConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String sellerPassword, ConstraintValidatorContext context) {
        return doSellerPasswordFormatCheck(sellerPassword);
    }

    private boolean doSellerPasswordFormatCheck(String sellerPassword) {
        return REGEX_PATTERN_SELLER_PASSWORD
                .matcher(sellerPassword)
                .matches();
    }
}