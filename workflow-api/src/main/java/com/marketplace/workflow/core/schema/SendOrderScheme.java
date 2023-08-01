package com.marketplace.workflow.core.schema;

import com.marketplace.workflow.core.gateway.ChangeCountProductGateway;
import com.marketplace.workflow.core.gateway.ChangeStatusOrderGateway;
import com.marketplace.workflow.core.gateway.RemoveProductFromBasketGateway;
import com.marketplace.workflow.core.operations.SendOrderOperation;
import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.steps.ChangeCountProductStep;
import com.marketplace.workflow.core.steps.ChangeStatusOrderStep;
import com.marketplace.workflow.core.steps.RemoveProductFromBasketStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendOrderScheme implements SchemaProvider<SendOrderOperation>{

    private final ChangeCountProductGateway productGateway;
    private final ChangeStatusOrderGateway orderGateway;
    private final RemoveProductFromBasketGateway basketGateway;

    @Override
    public Schema<SendOrderOperation> provideSchema() {

        return SchemaBuilder.builder()
                .step(changeCountProduct(),"changeCountProduct")
                .step(changeStatusOrder(), "changeStatusOrder")
                .step(removeProductFromBasket(), "")
                .build();
    }

    private AbstractStep<SendOrderOperation> changeCountProduct(){
        return new ChangeCountProductStep(productGateway);
    }

    private AbstractStep<SendOrderOperation> changeStatusOrder(){
        return new ChangeStatusOrderStep(orderGateway);
    }

    private AbstractStep<SendOrderOperation> removeProductFromBasket(){
        return new RemoveProductFromBasketStep(basketGateway);
    }

}
