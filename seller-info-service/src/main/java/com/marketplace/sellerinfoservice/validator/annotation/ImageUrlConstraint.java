package com.marketplace.sellerinfoservice.validator.annotation;

import com.marketplace.sellerinfoservice.validator.ImageUrlConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageUrlConstraintValidator.class)
public @interface ImageUrlConstraint {

    String message() default "Введённая сыслка на фото некорректная";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
