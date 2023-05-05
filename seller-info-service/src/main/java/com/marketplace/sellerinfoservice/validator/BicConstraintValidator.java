package com.marketplace.sellerinfoservice.validator;

import com.marketplace.sellerinfoservice.validator.annotation.BicConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class BicConstraintValidator implements ConstraintValidator<BicConstraint, String> {

    private static final Pattern REGEX_PATTERN_BIC = Pattern
            .compile("^[0-9]{9}$");

    @Override
    public void initialize(BicConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String bic, ConstraintValidatorContext context) {
        return doBicFormatCheck(bic);
    }

    private boolean doBicFormatCheck(String bic) {
        if (bic == null || bic.isEmpty()) {
            return true;
        }
        return REGEX_PATTERN_BIC
                .matcher(bic)
                .matches();
    }
}