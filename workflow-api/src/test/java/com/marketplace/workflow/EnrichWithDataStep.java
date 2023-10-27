package com.marketplace.workflow;

import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.steps.StepResult;

public class EnrichWithDataStep extends AbstractStep<TestOperation> {
    @Override
    public StepResult<TestOperation> apply(TestOperation testOperation) {
        testOperation.data("test");
        return StepResult.ok(testOperation, stepName());
    }
}
