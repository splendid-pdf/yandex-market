package com.marketplace.workflow.core.steps;

import com.marketplace.workflow.core.gateway.ChangeCountProductGateway;
import com.marketplace.workflow.core.operations.SendOrderOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class ChangeCountProductStep extends AbstractStep<SendOrderOperation> {

    private final ChangeCountProductGateway gateway;

    @Override
    public StepResult<SendOrderOperation> apply(SendOrderOperation sendOrderOperation) {

         ResponseEntity responseEntity = gateway.changeCountProduct(
                sendOrderOperation.sellerId(),
                sendOrderOperation.productCountDtoList()
        );

        System.out.println(responseEntity.getBody());

        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return StepResult.ok(sendOrderOperation,this.stepName());
        }



        return StepResult.failed(sendOrderOperation,this.stepName());
    }

    @Override
    public AbstractStep<SendOrderOperation> fallback(Supplier<FallbackResult> fallback) {
        return super.fallback(fallback);
    }

    @Override
    public Supplier<FallbackResult> fallback() {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        return super.fallback();
    }
}
