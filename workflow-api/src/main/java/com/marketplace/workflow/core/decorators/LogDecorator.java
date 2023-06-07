package com.marketplace.workflow.core.decorators;

import com.marketplace.workflow.core.Operation;
import com.marketplace.workflow.core.StepResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogDecorator<O extends Operation> implements Decorator<O> {

    private Decorator<O> decorator;

    @Override
    public StepResult<O> decorate(StepResult<O> stepResult) {
        log.info("Started to process '{}' step", stepResult.stepName());

        StepResult<O> result = getStepResult(stepResult);

        log.info("Finished to process '{}' step with '{}' result ", result.stepName(), result.state());

        return result;
    }
}
