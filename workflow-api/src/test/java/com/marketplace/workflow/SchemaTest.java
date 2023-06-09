package com.marketplace.workflow;

import com.marketplace.workflow.core.decorators.chains.BaseDecoratorChain;
import com.marketplace.workflow.core.operations.Operation;
import com.marketplace.workflow.core.schema.Schema;
import com.marketplace.workflow.core.schema.SchemaBuilder;
import com.marketplace.workflow.core.steps.AbstractStep;

import com.marketplace.workflow.core.steps.ErrorDetails;
import com.yandex.market.model.OperationResultType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SchemaTest {
    @Test
    void test_schemaWithoutSteps_shouldThrowIllegalArgumentException() {
        Schema<Operation> schema = SchemaBuilder.builder().build();

        assertThatThrownBy(() -> schema.apply(new Operation() {}))
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
            assertThat(((TestOperation)report.operation()).data()).isEqualTo("test");
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
            assertThat(((TestOperation)report.operation()).data()).isEqualTo("test");
            assertThat(((TestOperation)report.operation()).counter()).isEqualTo(1);
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
            assertThat(((TestOperation)report.operation()).data()).isEqualTo("test");
            assertThat(((TestOperation)report.operation()).counter()).isEqualTo(1);
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
            assertThat(((TestOperation)report.operation()).data()).isEqualTo("test");
            assertThat(((TestOperation)report.operation()).counter()).isEqualTo(1);
        });
    }
}
