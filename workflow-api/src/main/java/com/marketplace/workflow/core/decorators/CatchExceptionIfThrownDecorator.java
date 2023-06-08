package com.marketplace.workflow.core.decorators;

import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.steps.StepResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CatchExceptionIfThrownDecorator<O extends Operation> extends StepDecorator<O> {

    public CatchExceptionIfThrownDecorator(AbstractStep<O> decorator) {
        super(decorator);
    }

    @Override
    public StepResult<O> apply(O o) {
        try {
            return step.apply(o);
        } catch (Exception ex) {
            log.error("Step '{}' was failed with error message: {}", step.operationName(), ex.getMessage());
            return StepResult.failed(o, step.operationName());
        }
    }
}
