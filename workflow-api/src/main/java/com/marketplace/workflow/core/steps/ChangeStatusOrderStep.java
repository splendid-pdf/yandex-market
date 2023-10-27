package com.marketplace.workflow.core.steps;

import com.marketplace.workflow.core.gateway.ChangeStatusOrderGateway;
import com.marketplace.workflow.core.operations.SendOrderOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

@RequiredArgsConstructor
public class ChangeStatusOrderStep extends AbstractStep<SendOrderOperation>{

    private final ChangeStatusOrderGateway gateway;

    @Override
    public StepResult<SendOrderOperation> apply(SendOrderOperation operation) {
        HttpStatusCode httpStatusCode = gateway.changeStatusOrderOperation(
                operation.orderId()
        );

        if(httpStatusCode.is2xxSuccessful()){
            return StepResult.ok(operation,this.stepName());
        }

        return StepResult.failed(operation,this.stepName());
    }
}