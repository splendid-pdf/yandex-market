package com.marketplace.workflow.config;

import com.marketplace.workflow.core.Workflow;
import com.marketplace.workflow.core.operations.SendOrderOperation;
import com.marketplace.workflow.core.schema.SendOrderScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowConfiguration {
    @Bean
    public Workflow<SendOrderOperation> sendOrderOperationWorkflow(SendOrderScheme schema) {
        return new Workflow<>(schema);
    }
}
