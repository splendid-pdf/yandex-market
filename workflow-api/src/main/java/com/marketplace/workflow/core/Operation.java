package com.marketplace.workflow.core;

import com.marketplace.workflow.core.OperationResult;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public abstract class Operation {
    private OperationResult result;
}
