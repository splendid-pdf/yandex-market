package com.marketplace.sellerinfoservice.validator;

import com.marketplace.sellerinfoservice.validator.annotation.SellerLastNameConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SellerLastNameConstraintValidator implements ConstraintValidator<SellerLastNameConstraint, String> {

    private static final Pattern REGEX_PATTERN_SELLER_LAST_NAME = Pattern
            .compile("[а-яА-Яa-zA-Z]{2,100}");

    @Override
    public void initialize(SellerLastNameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String sellerLastName, ConstraintValidatorContext context) {
        return doSellerLastNameFormatCheck(sellerLastName);
    }

    private boolean doSellerLastNameFormatCheck(String sellerLastName) {
        if (sellerLastName == null || sellerLastName.isEmpty()) {
            return true;
        }
        return REGEX_PATTERN_SELLER_LAST_NAME
                .matcher(sellerLastName)
                .matches();
    }
}