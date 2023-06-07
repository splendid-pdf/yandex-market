package com.marketplace.workflow.core;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Workflow<O extends Operation> {

    private final SchemaProvider<O> schemaProvider;

    public O process(O o) {
        return schemaProvider.provideSchema(o).apply();
    }
}
