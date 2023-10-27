package com.marketplace.workflow.core.steps;

import com.marketplace.workflow.core.gateway.RemoveProductFromBasketGateway;
import com.marketplace.workflow.core.operations.SendOrderOperation;
import com.marketplace.workflow.dto.ProductCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@RequiredArgsConstructor
public class RemoveProductFromBasketStep extends AbstractStep<SendOrderOperation>{

    private final RemoveProductFromBasketGateway gateway;

    @Override
    public StepResult<SendOrderOperation> apply(SendOrderOperation sendOrderOperation) {
        List<String> productIds = sendOrderOperation.productCountDtoList().stream()
                .map(ProductCountDto::productId)
                .toList();

        HttpStatusCode httpStatusCode = gateway.removeProductFromBasket(
                sendOrderOperation.userId(),
                productIds
        );

        if(httpStatusCode.is2xxSuccessful()){
            return StepResult.ok(sendOrderOperation,this.stepName());
        }

        return StepResult.failed(sendOrderOperation,this.stepName());
    }
}
