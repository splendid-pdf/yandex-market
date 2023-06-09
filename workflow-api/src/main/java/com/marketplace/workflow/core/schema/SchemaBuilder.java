package com.marketplace.workflow.core.schema;

import com.marketplace.workflow.core.decorators.chains.DecoratorChain;
import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.resilience.Retry;
import com.marketplace.workflow.core.steps.AbstractStep;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SchemaBuilder<O extends Operation> {
    private final List<AbstractStep<O>> steps = new ArrayList<>();

    private Retry retry;
    private DecoratorChain<O> decoratorChain;

    public static <O extends Operation> SchemaBuilder<O> builder() {
        return new SchemaBuilder<>();
    }

    public SchemaBuilder<O> withDecoratorChain(DecoratorChain<O> decoratorChain) {
        this.decoratorChain = decoratorChain;
        return this;
    }

    public SchemaBuilder<O> step(AbstractStep<O> step) {
        steps.add(step);
        return this;
    }

    public SchemaBuilder<O> step(AbstractStep<O> step, String operationName) {
        step.stepName(operationName);
        steps.add(step);
        return this;
    }

    public SchemaBuilder<O> withRetry(Retry retry) {
        this.retry = retry;
        return this;
    }

    public Schema<O> build() {
        return new Schema<>(retry, steps, decoratorChain);
    }
}