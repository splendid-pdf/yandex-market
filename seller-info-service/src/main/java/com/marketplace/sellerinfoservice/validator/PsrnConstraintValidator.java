package com.marketplace.sellerinfoservice.validator;

import com.marketplace.sellerinfoservice.validator.annotation.PsrnConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PsrnConstraintValidator implements ConstraintValidator<PsrnConstraint, String> {

    private static final Pattern REGEX_PATTERN_PSRN = Pattern
            .compile("^[0-9]{15}$");

    @Override
    public void initialize(PsrnConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String psrn, ConstraintValidatorContext context) {
        return doPsrnFormatCheck(psrn);
    }

    private boolean doPsrnFormatCheck(String psrn) {
        if (psrn == null || psrn.isEmpty()) {
            return true;
        }
        return REGEX_PATTERN_PSRN
                .matcher(psrn)
                .matches();
    }
}