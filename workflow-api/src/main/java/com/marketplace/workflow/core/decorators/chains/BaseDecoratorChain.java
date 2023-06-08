package com.marketplace.workflow.core.decorators.chains;

import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.decorators.CatchExceptionIfThrownDecorator;
import com.marketplace.workflow.core.decorators.LogStepDecorator;
import com.marketplace.workflow.core.decorators.StepDecorator;
import com.marketplace.workflow.core.decorators.TimeTrackingStepDecorator;

public class BaseDecoratorChain<O extends Operation> {

    public StepDecorator<O> decorate(AbstractStep<O> step) {
        return
                new LogStepDecorator<>(
                    new TimeTrackingStepDecorator<>(
                        new CatchExceptionIfThrownDecorator<>(step)
                    )
                );
    }
}
