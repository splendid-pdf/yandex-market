package com.marketplace.sellerinfoservice.validator.annotation;

import com.marketplace.sellerinfoservice.validator.SellerPasswordConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SellerPasswordConstraintValidator.class)
public @interface SellerPasswordConstraint {

    String message() default "Введённый пароль невалиден";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}