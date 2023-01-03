package com.yandex.market.userinfoservice.validator;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface FieldValidator<T> {
    void validate(@NotNull T object, @NotNull List<String> exceptionMessages);
}
