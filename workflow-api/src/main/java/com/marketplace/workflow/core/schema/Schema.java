package com.marketplace.workflow.core.schema;

import com.marketplace.workflow.core.decorators.chains.DecoratorChain;
import com.marketplace.workflow.core.resilience.Retry;
import com.marketplace.workflow.core.steps.RollbackDetails;
import com.marketplace.workflow.core.steps.FallbackResult;
import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.operations.OperationProgressReport;
import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.steps.StepResult;
import com.yandex.market.model.OperationResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import static com.yandex.market.exception.TryCatch.tryCatch;
import static com.yandex.market.model.OperationResultType.FAILED;
import static com.yandex.market.model.OperationResultType.OK;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@RequiredArgsConstructor
public class Schema<O extends Operation> {
    private static final String DEFAULT_ERROR_MESSAGE = "Step was not rollbacked due the cause: %s";

    private final Retry retry;
    private final List<AbstractStep<O>> steps;
    private final DecoratorChain<O> decoratorChain;


    public OperationProgressReport apply(O o) {
        ListIterator<AbstractStep<O>> iterator = steps.listIterator();
        OperationProgressReport report = new OperationProgressReport();

        while (iterator.hasNext()) {
            AbstractStep<O> step = iterator.next();

            StepResult<O> result = runStep(o, step);

            if (result.isFailed()) {
                if (nonNull(retry) && tryRetry(o, step).isFailed()) {
                    List<RollbackDetails> rollbackDetails = rollback(iterator);
                    if (isNotEmpty(rollbackDetails)) {
                        return report.resultType(FAILED).details(rollbackDetails);
                    }
                }
            }
        }

        return report.resultType(OK);
    }

    private StepResult<O> runStep(O o, AbstractStep<O> step) {
        return nonNull(decoratorChain)
                ? decoratorChain.decorate(step).apply(o)
                : step.apply(o);
    }

    private StepResult<O> tryRetry(O o, AbstractStep<O> step) {
        return nonNull(retry.backoff())
                ? ForkJoinPool.commonPool()
                        .submit(() -> retryProcess(o, step))
                        .join()
                : retryProcess(o, step);
    }

    @SneakyThrows
    private StepResult<O> retryProcess(O o, AbstractStep<O> step) {
        int retryAttempts = retry.maximumAttempts();
        while (retryAttempts > 0) {
            waitIfBackoffWasSetup();

            StepResult<O> retryResult = runStep(o, step);
            if (retryResult.isOk()) {
                return retryResult;
            }
            retryAttempts--;
        }

        return StepResult.failed(o, step.stepName());
    }

    private void waitIfBackoffWasSetup() throws InterruptedException {
        if (nonNull(retry.backoff())) {
            TimeUnit.NANOSECONDS.wait(retry.backoff().getDelayInNanos());
        }
    }

    private List<RollbackDetails> rollback(ListIterator<AbstractStep<O>> iterator) {
        List<RollbackDetails> details = Collections.emptyList();
        while (iterator.hasPrevious()) {
            AbstractStep<O> step = iterator.previous();
            OperationResponse operationResponse = tryCatch(step.fallback());
            FallbackResult fallbackResult = (FallbackResult) operationResponse.operationResult();
            if (fallbackResult == FallbackResult.FAILED) {
                if (details.isEmpty()) {
                    details = new ArrayList<>();
                }

                details.add(
                        new RollbackDetails(
                            step.stepName(),
                            DEFAULT_ERROR_MESSAGE.formatted(operationResponse.errorMessage())
                        )
                );
            }
        }

        return details;
    }
}
