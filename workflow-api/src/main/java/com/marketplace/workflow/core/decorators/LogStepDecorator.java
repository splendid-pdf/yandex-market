package com.marketplace.workflow.core.decorators;

import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.steps.StepResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogStepDecorator<O extends Operation> extends StepDecorator<O> {
    public LogStepDecorator(AbstractStep<O> step) {
        super(step);
    }

    @Override
    public StepResult<O> apply(O o) {
        log.info("Started to process '{}' step", this.stepName());

        StepResult<O> result = step.apply(o);

        log.info("Finished to process '{}' step with '{}' result ", result.stepName(), result.state());

        return result;
    }
}
