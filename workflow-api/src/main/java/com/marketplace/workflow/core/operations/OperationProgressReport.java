package com.marketplace.workflow.core.operations;

import com.marketplace.workflow.core.steps.ErrorDetails;
import com.yandex.market.model.OperationResultType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true, fluent = true)
public class OperationProgressReport {
    private List<ErrorDetails> details;
    private OperationResultType resultType;
}
