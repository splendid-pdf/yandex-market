package com.marketplace.workflow;

import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.steps.StepResult;

import java.util.Objects;

public class IncreaseCountStep extends AbstractStep<TestOperation> {
    @Override
    public StepResult<TestOperation> apply(TestOperation testOperation) {
        testOperation.counter(Objects.isNull(testOperation.counter)
                ? 1
                : testOperation.counter + 1
        );
        return StepResult.ok(testOperation, stepName());
    }
}
