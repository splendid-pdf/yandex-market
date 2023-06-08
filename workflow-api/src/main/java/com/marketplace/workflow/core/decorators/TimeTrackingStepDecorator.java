package com.marketplace.workflow.core.decorators;

import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.steps.StepResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeTrackingStepDecorator<O extends Operation> extends StepDecorator<O> {

    public TimeTrackingStepDecorator(AbstractStep<O> step) {
        super(step);
    }

    @Override
    public StepResult<O> apply(O o) {
        long start = System.nanoTime();

        StepResult<O> result = step.apply(o);

        long end = System.nanoTime();

        if (result.isFailed()) {
            return result;
        }

        log.info("Step '{}' completed in {} nanoseconds", result.stepName(), (end - start));

        return result;
    }
}
