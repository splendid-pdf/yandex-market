package com.marketplace.sellerinfoservice.validator.annotation;

import com.marketplace.sellerinfoservice.validator.CorporateAccountConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorporateAccountConstraintValidator.class)
public @interface CorporateAccountConstraint {

    String message() default "Введённый банковский корпаративный счет некорректный";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}