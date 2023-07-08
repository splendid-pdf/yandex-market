package com.marketplace.workflow.core.schema;

import com.marketplace.workflow.core.gateway.ChangeCountProductGateway;
import com.marketplace.workflow.core.gateway.ChangeStatusOrderGateway;
import com.marketplace.workflow.core.operations.ChangeCountProductOperation;
import com.marketplace.workflow.core.operations.SendOrderOperation;
import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.steps.ChangeCountProductStep;
import com.marketplace.workflow.core.steps.ChangeStatusOrderStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendOrdertScheme implements SchemaProvider<SendOrderOperation>{

    private final ChangeCountProductGateway productGateway;
    private final ChangeStatusOrderGateway orderGateway;

    @Override
    public Schema<SendOrderOperation> provideSchema() {

        return SchemaBuilder.builder()
                .step(changeCountProduct(),"changeCountProduct")
                .step(changeStatusOrder(), "changeStatusOrder")
                .build();
    }

    private AbstractStep<ChangeCountProductOperation> changeCountProduct(){
        return new ChangeCountProductStep(productGateway);
    }

    private AbstractStep<SendOrderOperation> changeStatusOrder(){
        return new ChangeStatusOrderStep(orderGateway);
    }

}
