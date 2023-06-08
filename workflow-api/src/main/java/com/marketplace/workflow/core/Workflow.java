package com.marketplace.workflow.core;

import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.operations.OperationProgressReport;
import com.marketplace.workflow.core.schema.SchemaProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Workflow<O extends Operation> {

    private final SchemaProvider<O> schemaProvider;

    public OperationProgressReport process(O o) {
        return schemaProvider.provideSchema(o).apply();
    }
}
