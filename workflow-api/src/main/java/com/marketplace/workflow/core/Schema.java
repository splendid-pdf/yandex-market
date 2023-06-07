package com.marketplace.workflow.core;

import com.marketplace.workflow.core.decorators.Decorator;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.ListIterator;

@RequiredArgsConstructor
public class Schema<O extends Operation> {
    private final O o;
    private final Decorator<O> decorator;
    private final List<AbstractStep<O>> steps;

    public O apply() {
        ListIterator<AbstractStep<O>> iterator = steps.listIterator();

        while (iterator.hasNext()) {
            AbstractStep<O> step = iterator.next();
            step.operation(o);

            StepResult<O> result = decorator.decorate(step.apply());

            if (result.state() != StepState.OK) {
                rollback(iterator);
                o.result(OperationResult.FAILED);
                return o;
            }
        }

        o.result(OperationResult.OK);

        return o;
    }

    private void rollback(ListIterator<AbstractStep<O>> iterator) {
        while (iterator.hasPrevious()) {
            AbstractStep<O> step = iterator.previous();
            FallbackResult fallbackResult = step.fallback().get();

            // TODO: consider retry
            if (fallbackResult == FallbackResult.FAILED) {

            }
        }
    }
}
