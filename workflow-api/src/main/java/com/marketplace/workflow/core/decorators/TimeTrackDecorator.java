package com.marketplace.workflow.core.decorators;

import com.marketplace.workflow.core.Operation;
import com.marketplace.workflow.core.StepResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTrackDecorator<O extends Operation> implements Decorator<O> {
    private Decorator<O> decorator;

    @Override
    public StepResult<O> decorate(StepResult<O> result) {

        long start = System.nanoTime();
        result = getStepResult(result);
        long end = System.nanoTime();

        log.info("Step '{}' completed in {} nanoseconds", result.stepName(), (end - start));

        return result;
    }
}
