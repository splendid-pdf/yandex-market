package com.marketplace.workflow;

import com.marketplace.workflow.core.decorators.chains.BaseDecoratorChain;
import com.marketplace.workflow.core.gateway.ChangeCountProductGateway;
import com.marketplace.workflow.core.operations.ChangeCountProductOperation;
import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.schema.Schema;
import com.marketplace.workflow.core.schema.SchemaBuilder;
import com.marketplace.workflow.core.steps.AbstractStep;

import com.marketplace.workflow.core.steps.ChangeCountProductStep;
import com.marketplace.workflow.core.steps.ErrorDetails;
import com.yandex.market.model.OperationResultType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;

public class SchemaTest {


    private final ChangeCountProductGateway gateway = new ChangeCountProductGateway(new RestTemplate());

    @Test
    void test_schemaWithoutSteps_shouldThrowIllegalArgumentException() {
        Schema<Operation> schema = SchemaBuilder.builder().build();

        assertThatThrownBy(() -> schema.apply(new Operation() {
        }))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Steps were not configured");
    }

    @Test
    void test_schemaWithOnlyOneDummyStep_shouldBeOk() {
        AbstractStep<TestOperation> updatePersonStep = new DummyStep();

        Schema<Operation> schema = SchemaBuilder.builder()
                .step(updatePersonStep, "updatePersonStep")
                .build();

        assertThat(schema.apply(new TestOperation())).satisfies(report -> {
            assertThat(report.errorDetails()).isNull();
            assertThat(report.resultType()).isEqualTo(OperationResultType.OK);
        });
    }

    @Test
    void test_schemaWithOnlyOneStep_shouldBeOk() {
        AbstractStep<TestOperation> enrichWithDataStep = new EnrichWithDataStep();

        Schema<Operation> schema = SchemaBuilder.builder()
                .step(enrichWithDataStep, "enrichWithDataStep")
                .build();

        assertThat(schema.apply(new TestOperation())).satisfies(report -> {
            assertThat(report.errorDetails()).isNull();
            assertThat(report.resultType()).isEqualTo(OperationResultType.OK);
            assertThat(((TestOperation) report.operation()).data()).isEqualTo("test");
        });
    }

    @Test
    void testProduct_schemaWithOnlyOneStep_shouldBeOk() {
        AbstractStep<ChangeCountProductOperation> productOperationAbstractStep =
                new ChangeCountProductStep(gateway);

        Schema<Operation> schema = SchemaBuilder.builder()
                .step(productOperationAbstractStep, "productOperationAbstractStep")
                .build();

        assertThat(schema.apply(new ChangeCountProductOperation(
                        "8fa0a3ef-cb2a-4c07-ade4-efac2756522d",
                        "8aa57ad3-1bef-4aab-841f-1cf23893546c",
                        -20L)))
                .satisfies(report -> {
                    assertThat(report.errorDetails()).isNull();
                    assertThat(report.resultType()).isEqualTo(OperationResultType.OK);
                });
    }

    @Test
    void test_schemaWithTwoSteps_shouldBeOk() {
        AbstractStep<TestOperation> enrichWithDataStep = new EnrichWithDataStep();
        AbstractStep<TestOperation> increaseCountStep = new IncreaseCountStep();

        Schema<Operation> schema = SchemaBuilder.builder()
                .step(enrichWithDataStep, "enrichWithDataStep")
                .step(increaseCountStep, "increaseCountStep")
                .build();

        assertThat(schema.apply(new TestOperation())).satisfies(report -> {
            assertThat(report.errorDetails()).isNull();
            assertThat(report.resultType()).isEqualTo(OperationResultType.OK);
            assertThat(((TestOperation) report.operation()).data()).isEqualTo("test");
            assertThat(((TestOperation) report.operation()).counter()).isEqualTo(1);
        });
    }

    @Test
    void test_schemaWithThreeStepsAndLastIsFailed_shouldBeFailed() {
        AbstractStep<TestOperation> enrichWithDataStep = new EnrichWithDataStep();
        AbstractStep<TestOperation> increaseCountStep = new IncreaseCountStep();
        AbstractStep<TestOperation> failStep = new FailStep();

        Schema<Operation> schema = SchemaBuilder.builder()
                .step(enrichWithDataStep, "enrichWithDataStep")
                .step(increaseCountStep, "increaseCountStep")
                .step(failStep, "failStep")
                .build();

        assertThat(schema.apply(new TestOperation())).satisfies(report -> {
            assertThat(report.errorDetails()).isNull();
            assertThat(report.resultType()).isEqualTo(OperationResultType.FAILED);
            assertThat(((TestOperation) report.operation()).data()).isEqualTo("test");
            assertThat(((TestOperation) report.operation()).counter()).isEqualTo(1);
        });
    }

    @Test
    void test_schemaWithDecoratorsAndThreeStepsAndLastIsFailed_shouldBeFailed() {
        AbstractStep<TestOperation> enrichWithDataStep = new EnrichWithDataStep();
        AbstractStep<TestOperation> increaseCountStep = new IncreaseCountStep();
        AbstractStep<TestOperation> failStep = new FailStep();

        Schema<Operation> schema = SchemaBuilder.builder()
                .withDecoratorChain(new BaseDecoratorChain<TestOperation>())
                .step(enrichWithDataStep, "enrichWithDataStep")
                .step(increaseCountStep, "increaseCountStep")
                .step(failStep, "failStep")
                .build();

        assertThat(schema.apply(new TestOperation())).satisfies(report -> {
            assertThat(report.errorDetails()).isNull();
            assertThat(report.resultType()).isEqualTo(OperationResultType.FAILED);
            assertThat(((TestOperation) report.operation()).data()).isEqualTo("test");
            assertThat(((TestOperation) report.operation()).counter()).isEqualTo(1);
        });
    }
}
