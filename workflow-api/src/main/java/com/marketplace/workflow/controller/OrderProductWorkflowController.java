package com.marketplace.workflow.controller;

import com.marketplace.workflow.core.Workflow;
import com.marketplace.workflow.core.operations.ChangeCountProductOperation;
import com.marketplace.workflow.core.operations.OperationProgressReport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@RestController
@RequiredArgsConstructor
@RequestMapping(PUBLIC_API_V1)
public class OrderProductWorkflowController {

    private final Workflow<ChangeCountProductOperation> changeCountProductOperationWorkflow;

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/sellers/orders/{orderId}/send")
    public ResponseEntity<OperationProgressReport> changeCountProduct(ChangeCountProductOperation operation, @PathVariable String orderId) {
        return ResponseEntity.ok(changeCountProductOperationWorkflow.process(operation));
    }

//    private ResponseEntity<OperationProgressReport> changeCountProduct(ChangeCountProductOperation operation) {
//        return ResponseEntity.ok(changeCountProductOperationWorkflow.process(operation));
//    }
//
//
//    @ResponseStatus(HttpStatus.OK)
//    @PatchMapping("/sellers/orders/{orderId}/send")
//    public void sendOrder(@Parameter(name = "orderId", description = "Order's identifier")
//                          @PathVariable("orderId") UUID orderId) {
//        changeCountProduct(new ChangeCountProductOperation(
//                        "8fa0a3ef-cb2a-4c07-ade4-efac2756522d",
//                        "8aa57ad3-1bef-4aab-841f-1cf23893546c",
//                        -20L
//                )
//        );
//    }
}

