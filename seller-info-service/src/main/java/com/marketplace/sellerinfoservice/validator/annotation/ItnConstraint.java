package com.marketplace.sellerinfoservice.validator.annotation;

import com.marketplace.sellerinfoservice.validator.ItnConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ItnConstraintValidator.class)
public @interface ItnConstraint {

    String message() default "Введённый идентификационный номер налогоплательщика некорректный";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}