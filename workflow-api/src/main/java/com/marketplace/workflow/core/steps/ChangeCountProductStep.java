package com.marketplace.workflow.core.steps;

import com.marketplace.workflow.core.gateway.ChangeCountProductGateway;
import com.marketplace.workflow.core.operations.ChangeCountProductOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

@RequiredArgsConstructor
public class ChangeCountProductStep extends AbstractStep<ChangeCountProductOperation> {


    private final ChangeCountProductGateway gateway;


    @Override
    public StepResult<ChangeCountProductOperation> apply(ChangeCountProductOperation changeCountProductOperation) {

        HttpStatusCode httpStatusCode = gateway.changeCountProduct(
                changeCountProductOperation.sellerId(),
                changeCountProductOperation.productId(),
                changeCountProductOperation.count()
        );

        if(httpStatusCode.is2xxSuccessful()){
            return StepResult.ok(changeCountProductOperation,this.stepName());
        }

        return StepResult.failed(changeCountProductOperation,this.stepName());
    }
}
