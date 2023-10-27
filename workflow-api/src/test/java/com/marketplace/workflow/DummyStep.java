package com.marketplace.workflow;

import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.steps.StepResult;

public class DummyStep extends AbstractStep<TestOperation> {
    public DummyStep() {
    }

    @Override
    public StepResult<TestOperation> apply(TestOperation testOperation) {
        return StepResult.ok(testOperation, this.stepName());
    }
}