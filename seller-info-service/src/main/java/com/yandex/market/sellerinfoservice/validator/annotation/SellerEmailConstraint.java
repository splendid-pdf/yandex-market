package com.yandex.market.sellerinfoservice.validator.annotation;

import com.yandex.market.sellerinfoservice.validator.SellerEmailConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SellerEmailConstraintValidator.class)
public @interface SellerEmailConstraint {

    String message() default "Введённая электронная почта некорректна";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
