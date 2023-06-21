package com.marketplace.workflow.config;

import com.marketplace.workflow.core.Workflow;
import com.marketplace.workflow.core.operations.ChangeCountProductOperation;
import com.marketplace.workflow.core.schema.ChangeCountProductScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowConfiguration {
    @Bean
    public Workflow<ChangeCountProductOperation> changeCountProductOperationWorkflow(ChangeCountProductScheme schema) {
        return new Workflow<>(schema);
    }
}
