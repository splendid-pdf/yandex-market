package com.marketplace.workflow.core.decorators;

import com.marketplace.workflow.core.Operation;
import com.marketplace.workflow.core.StepResult;

import static java.util.Objects.nonNull;

public interface Decorator<O extends Operation> {

    Decorator<O> getDecorator();

    StepResult<O> decorate(StepResult<O> result);

    default StepResult<O> getStepResult(StepResult<O> result) {
        return nonNull(this.getDecorator())
                ? this.getDecorator().decorate(result)
                : result;
    }
}
