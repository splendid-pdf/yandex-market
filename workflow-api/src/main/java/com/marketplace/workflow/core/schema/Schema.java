package com.marketplace.workflow.core.schema;

import com.marketplace.workflow.core.decorators.chains.DecoratorChain;
import com.marketplace.workflow.core.steps.ErrorDetails;
import com.marketplace.workflow.core.steps.FallbackResult;
import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.operations.OperationProgressReport;
import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.steps.StepResult;
import com.marketplace.workflow.core.steps.StepState;
import com.yandex.market.model.OperationResponse;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import static com.yandex.market.exception.TryCatch.tryCatch;
import static com.yandex.market.model.OperationResultType.FAILED;
import static com.yandex.market.model.OperationResultType.OK;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@RequiredArgsConstructor
public class Schema<O extends Operation> {
    private static final String DEFAULT_ERROR_MESSAGE = "Step was not rollbacked due the cause: %s";

    private final O o;
    private final List<AbstractStep<O>> steps;
    private final DecoratorChain<O> decoratorChain;

    public OperationProgressReport apply() {
        ListIterator<AbstractStep<O>> iterator = steps.listIterator();
        OperationProgressReport report = new OperationProgressReport();

        while (iterator.hasNext()) {
            AbstractStep<O> step = iterator.next();

            StepResult<O> result = nonNull(decoratorChain)
                    ? decoratorChain.decorate(step).apply(o)
                    : step.apply(o);

            if (result.state() != StepState.OK) {
                List<ErrorDetails> errorDetails = rollback(iterator);
                if (isNotEmpty(errorDetails)) {
                     return report.resultType(FAILED).details(errorDetails);
                }
            }
        }

        return report.resultType(OK);
    }

    private List<ErrorDetails> rollback(ListIterator<AbstractStep<O>> iterator) {
        List<ErrorDetails> details = Collections.emptyList();
        while (iterator.hasPrevious()) {
            AbstractStep<O> step = iterator.previous();
            OperationResponse operationResponse = tryCatch(step.fallback());
            FallbackResult fallbackResult = (FallbackResult) operationResponse.operationResult();
            if (fallbackResult == FallbackResult.FAILED) {
                if (details.isEmpty()) {
                    details = new ArrayList<>();
                }

                details.add(
                        new ErrorDetails(
                            step.operationName(),
                            DEFAULT_ERROR_MESSAGE.formatted(operationResponse.errorMessage())
                        )
                );
            }
        }

        return details;
    }
}
