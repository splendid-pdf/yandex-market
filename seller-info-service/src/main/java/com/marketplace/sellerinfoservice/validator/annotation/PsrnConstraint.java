package com.marketplace.sellerinfoservice.validator.annotation;

import com.marketplace.sellerinfoservice.validator.PsrnConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PsrnConstraintValidator.class)
public @interface PsrnConstraint {

    String message() default "Введённый основной государственный регистрационный номер некорректный";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}