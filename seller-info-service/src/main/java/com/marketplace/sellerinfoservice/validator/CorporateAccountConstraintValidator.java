package com.marketplace.sellerinfoservice.validator;

import com.marketplace.sellerinfoservice.validator.annotation.CorporateAccountConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CorporateAccountConstraintValidator implements ConstraintValidator<CorporateAccountConstraint, String> {

    private static final Pattern REGEX_PATTERN_CORPORATE_ACCOUNT = Pattern
            .compile("^[0-9]{20}$");

    @Override
    public void initialize(CorporateAccountConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String corporateAccount, ConstraintValidatorContext context) {
        return doCorporateAccountFormatCheck(corporateAccount);
    }

    private boolean doCorporateAccountFormatCheck(String corporateAccount) {
        if (corporateAccount == null || corporateAccount.isEmpty()) {
            return true;
        }
        return REGEX_PATTERN_CORPORATE_ACCOUNT
                .matcher(corporateAccount)
                .matches();
    }
}