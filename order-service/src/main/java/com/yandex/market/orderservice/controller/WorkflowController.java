package com.yandex.market.orderservice.controller;


import com.marketplace.workflow.core.Workflow;
import com.marketplace.workflow.core.operations.ChangeCountProductOperation;
import com.marketplace.workflow.core.operations.OperationProgressReport;
import com.yandex.market.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@RestController
@RequestMapping(PUBLIC_API_V1)
@RequiredArgsConstructor
public class WorkflowController {

    private final Workflow<ChangeCountProductOperation> changeCountProductOperationWorkflow;
    private final OrderService orderService;


    private ResponseEntity<OperationProgressReport> changeCountProduct(ChangeCountProductOperation operation) {
        return ResponseEntity.ok(changeCountProductOperationWorkflow.process(operation));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/sellers/orders/{orderId}/send")
    public void sendOrder(@Parameter(name = "orderId", description = "Order's identifier")
                          @PathVariable("orderId") UUID orderId) {
        changeCountProduct(new ChangeCountProductOperation(
                "8fa0a3ef-cb2a-4c07-ade4-efac2756522d",
                "8aa57ad3-1bef-4aab-841f-1cf23893546c",
                -20L
                )
        );
    }
}
