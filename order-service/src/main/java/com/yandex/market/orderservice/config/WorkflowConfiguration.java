package com.yandex.market.orderservice.config;

import com.marketplace.workflow.core.Workflow;
import com.marketplace.workflow.core.operations.ChangeCountProductOperation;
import com.marketplace.workflow.core.schema.ChangeCountProductScheme;
import com.marketplace.workflow.core.schema.Schema;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowConfiguration {
    @Bean
    public Workflow<ChangeCountProductOperation> changeCountProductOperationWorkflow(ChangeCountProductScheme schema) {
        return new Workflow<>(schema);
    }


}
