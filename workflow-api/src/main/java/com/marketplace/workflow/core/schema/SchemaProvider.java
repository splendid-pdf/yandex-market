package com.marketplace.workflow.core.schema;

import com.marketplace.workflow.core.operations.Operation;

public interface SchemaProvider<O extends Operation> {
    Schema<O> provideSchema(O o);
}
