package com.marketplace.workflow.core.schema;

import com.marketplace.workflow.core.gateway.ChangeCountProductGateway;
import com.marketplace.workflow.core.operations.ChangeCountProductOperation;
import com.marketplace.workflow.core.steps.AbstractStep;
import com.marketplace.workflow.core.steps.ChangeCountProductStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeCountProductScheme implements SchemaProvider<ChangeCountProductOperation>{

    private final ChangeCountProductGateway gateway;

    @Override
    public Schema<ChangeCountProductOperation> provideSchema() {

        return SchemaBuilder.builder()
                .step(changeCountProduct(),"changeCountProduct")
                .build();
    }

    private AbstractStep<ChangeCountProductOperation> changeCountProduct(){
        return new ChangeCountProductStep(gateway);
    }
}
