package com.marketplace.workflow.core.decorators.chains;

import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.decorators.StepDecorator;

public interface DecoratorChain<O extends Operation> {

    StepDecorator<O> decorate(AbstractStep<O> step);
}