package com.marketplace.sellerinfoservice.validator.annotation;

import com.marketplace.sellerinfoservice.validator.BicConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BicConstraintValidator.class)
public @interface BicConstraint {

    String message() default "Введённый банковский идентификационный код некорректный";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}