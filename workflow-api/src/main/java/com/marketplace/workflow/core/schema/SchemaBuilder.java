package com.marketplace.workflow.core.schema;

import com.marketplace.workflow.core.decorators.chains.BaseDecoratorChain;
import com.marketplace.workflow.core.decorators.chains.DecoratorChain;
import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.steps.AbstractStep;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SchemaBuilder<O extends Operation> {
    private final O o;
    private final List<AbstractStep<O>> steps = new ArrayList<>();

    private DecoratorChain<O> decoratorChain;

    public static <O extends Operation> SchemaBuilder<O> builder(O o) {
        return new SchemaBuilder<O>(o);
    }

    public SchemaBuilder<O> decoratorChain(DecoratorChain<O> decoratorChain) {
        this.decoratorChain = decoratorChain;
        return this;
    }
    public SchemaBuilder<O> step(AbstractStep<O> step, String operationName) {
        step.operationName(operationName);
        steps.add(step);
        return this;
    }

    public Schema<O> build() {
        return new Schema<>(o, steps, decoratorChain);
    }
}