package com.marketplace.sellerinfoservice.validator;

import com.marketplace.sellerinfoservice.validator.annotation.SellerFirstNameConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SellerFirstNameConstraintValidator implements ConstraintValidator<SellerFirstNameConstraint, String> {

    private static final Pattern REGEX_PATTERN_SELLER_FIRST_NAME = Pattern
            .compile("[а-яА-Яa-zA-Z]{2,100}");

    @Override
    public void initialize(SellerFirstNameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String sellerFirstName, ConstraintValidatorContext context) {
        return doSellerFirstNameFormatCheck(sellerFirstName);
    }

    private boolean doSellerFirstNameFormatCheck(String sellerFirstName) {
        if (sellerFirstName == null || sellerFirstName.isEmpty()) {
            return true;
        }
        return REGEX_PATTERN_SELLER_FIRST_NAME
                .matcher(sellerFirstName)
                .matches();
    }
}