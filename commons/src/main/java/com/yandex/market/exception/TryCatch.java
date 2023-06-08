package com.yandex.market.exception;

import com.yandex.market.model.OperationResponse;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

import static com.yandex.market.model.OperationResultType.FAILED;
import static com.yandex.market.model.OperationResultType.OK;

@UtilityClass
public class TryCatch {

    public static OperationResponse tryCatch(Supplier<?> supplier) {
        try {
            Object result = supplier.get();

            return new OperationResponse()
                    .type(OK)
                    .source(supplier)
                    .operationResult(result);
        } catch (Exception ex) {
            return new OperationResponse()
                    .type(FAILED)
                    .source(supplier)
                    .errorMessage(ex.getMessage());
        }
    }
}
