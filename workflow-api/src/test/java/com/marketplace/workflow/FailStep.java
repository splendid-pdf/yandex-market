package com.marketplace.workflow;

import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.steps.StepResult;

public class FailStep extends AbstractStep<TestOperation> {
    @Override
    public StepResult<TestOperation> apply(TestOperation testOperation) {
        return StepResult.failed(testOperation, stepName());
    }
}
