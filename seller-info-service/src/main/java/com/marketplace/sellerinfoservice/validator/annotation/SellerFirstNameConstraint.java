package com.marketplace.sellerinfoservice.validator.annotation;

import com.marketplace.sellerinfoservice.validator.SellerFirstNameConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SellerFirstNameConstraintValidator.class)
public @interface SellerFirstNameConstraint {

    String message() default "Введённое имя некорректное";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
