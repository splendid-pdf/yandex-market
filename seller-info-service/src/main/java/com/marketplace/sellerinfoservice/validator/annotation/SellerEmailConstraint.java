package com.marketplace.sellerinfoservice.validator.annotation;

import com.marketplace.sellerinfoservice.validator.SellerEmailConstraintValidator;
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

    String message() default "Введённая электронная почта некорректна," +
            "должна соответствовать ^(?=.{1,50}@)[a-zA-Z0-9_!#$%&'+=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
