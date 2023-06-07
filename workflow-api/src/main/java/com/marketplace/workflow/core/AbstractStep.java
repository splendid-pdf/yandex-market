package com.marketplace.workflow.core;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Supplier;

@Getter
@Setter
@Accessors(fluent = true)
public abstract class AbstractStep<O> {

    private O operation;
    private String operationName;
    private Supplier<FallbackResult> fallback;

    public abstract StepResult<O> apply();
}
