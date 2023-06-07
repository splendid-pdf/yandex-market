package com.marketplace.workflow.core;

public interface SchemaProvider<O extends Operation> {
    Schema<O> provideSchema(O o);
}
