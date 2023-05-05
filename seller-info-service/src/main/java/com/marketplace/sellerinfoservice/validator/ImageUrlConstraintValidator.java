package com.marketplace.sellerinfoservice.validator;

import com.marketplace.sellerinfoservice.validator.annotation.ImageUrlConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ImageUrlConstraintValidator implements ConstraintValidator<ImageUrlConstraint, String> {


    private static final Pattern REGEX_PATTERN_IMAGE_URL = Pattern
            .compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");

    @Override
    public void initialize(ImageUrlConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String imageUrl, ConstraintValidatorContext context) {
        return doSellerImageUrlFormatCheck(imageUrl);
    }

    private boolean doSellerImageUrlFormatCheck(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return true;
        }
        return REGEX_PATTERN_IMAGE_URL
                .matcher(imageUrl)
                .matches();
    }
}