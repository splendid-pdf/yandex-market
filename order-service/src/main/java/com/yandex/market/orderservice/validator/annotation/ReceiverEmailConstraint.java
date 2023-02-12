package com.yandex.market.orderservice.validator.annotation;

import com.yandex.market.orderservice.validator.ReceiverEmailConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReceiverEmailConstraintValidator.class)
public @interface ReceiverEmailConstraint {
    String message() default "Введённая электронная почта получателя некорректна";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}