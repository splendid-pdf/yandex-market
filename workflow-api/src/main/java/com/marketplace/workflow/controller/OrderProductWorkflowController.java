package com.marketplace.workflow.controller;

import com.marketplace.workflow.core.Workflow;
import com.marketplace.workflow.core.operations.SendOrderOperation;
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

    //todo: заметки сумасшедшего
    /**
     * заметки сумасшедшего
     * принимаем в контроллере dto, его конвертируем в operation. Вопрос, что должно быть вутри дто
     * чтобы его потом нормально конвертнуть. Может ли шаг вернуть какие-то данные? Если да, то мы можем
     * с фронта получить только ордер ид, и сделать первый шаг, изменить статус в ордер сервисе,
     * и из него вернуть селлер ид, и продукт ид, которые нужны для второго шага
     *
     * Короче всё должно приходить с фронта для выполнения всех шагов
     *
     * План на завтра
     * нам нужно сделать ещё один шаг, который будет хотить в ордлер сервис и менять статус.
     * провести испытания
     * научится работать с патч методом
     * добавить респонс энтити, для работы с патчем
     * гетвай
     * степы сделать
     */

    private final Workflow<SendOrderOperation> changeStatusOrderWorkflow;

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/sellers/orders/send")
    public ResponseEntity<OperationProgressReport> changeCountProduct(SendOrderOperation operation) {
        return ResponseEntity.ok(changeStatusOrderWorkflow.process(operation));
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

