package com.marketplace.sellerinfoservice.validator;

import com.marketplace.sellerinfoservice.validator.annotation.ItnConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ItnConstraintValidator implements ConstraintValidator<ItnConstraint, String> {

    private static final Pattern REGEX_PATTERN_ITN = Pattern
            .compile("^[0-9]{10}$");

    @Override
    public void initialize(ItnConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String itn, ConstraintValidatorContext context) {
        return doItnFormatCheck(itn);
    }

    private boolean doItnFormatCheck(String itn) {
        if (itn == null || itn.isEmpty()) {
            return true;
        }
        return REGEX_PATTERN_ITN
                .matcher(itn)
                .matches();
    }
}