package com.yandex.market.orderservice.validator.annotation;

import com.yandex.market.orderservice.validator.ReceiverNameConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReceiverNameConstraintValidator.class)
public @interface ReceiverNameConstraint {
    String message() default "Введеённое имя получателя некорректное";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}