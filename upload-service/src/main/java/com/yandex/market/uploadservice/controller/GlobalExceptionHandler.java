package com.yandex.market.uploadservice.controller;

import com.yandex.market.exception.SizeLimitFileExceededException;
import com.yandex.market.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SizeLimitFileExceededException.class)
    public ErrorResponse handleSizeLimitException(SizeLimitFileExceededException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                UUID.randomUUID(),
                OffsetDateTime.now()
        );
        log.debug("Error with exceeding the maximum file size with this id was handled = {}", errorResponse.errorId());
        return errorResponse;
    }


}
