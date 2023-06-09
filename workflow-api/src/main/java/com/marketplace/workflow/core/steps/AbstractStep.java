package com.marketplace.workflow.core.steps;

import com.marketplace.workflow.core.operations.Operation;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
@Setter
public abstract class AbstractStep<O extends Operation> {

    private String stepName;
    private Supplier<FallbackResult> fallback;

    public abstract StepResult<O> apply(O o);
}
