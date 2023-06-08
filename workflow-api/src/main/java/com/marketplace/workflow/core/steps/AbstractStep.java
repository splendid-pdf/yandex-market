package com.marketplace.workflow.core.steps;

import com.marketplace.workflow.core.operations.Operation;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Supplier;

@Getter
@Setter
@Accessors(fluent = true)
public abstract class AbstractStep<O extends Operation> {

    private String operationName;
    private Supplier<FallbackResult> fallback;

    public abstract StepResult<O> apply(O o);
}
