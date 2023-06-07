package com.marketplace.workflow.core;

import com.marketplace.workflow.core.decorators.Decorator;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SchemaBuilder<O extends Operation> {
    private final O o;
    private final List<AbstractStep<O>> steps = new ArrayList<>();

    private Decorator<O> decorator;

    public static <O extends Operation> SchemaBuilder<O> builder(O o) {
        return new SchemaBuilder<O>(o);
    }

    public SchemaBuilder<O> decorator(Decorator<O> decorator) {
        this.decorator = decorator;
        return this;
    }
    public SchemaBuilder<O> step(AbstractStep<O> step, String operationName) {
        step.operationName(operationName);
        steps.add(step);
        return this;
    }

    public Schema<O> build() {
        return new Schema<>(o, decorator, steps);
    }
}