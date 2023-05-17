package com.marketplace.sellerinfoservice.controller;

import com.marketplace.sellerinfoservice.controller.errorResponse.ValidationErrorResponse;
import com.marketplace.sellerinfoservice.controller.errorResponse.Violation;
import com.yandex.market.model.ErrorResponse;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class SellerInfoExceptionHandler {

    private final Tracer tracer;

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityExistsException.class)
    public ErrorResponse entityExistExceptionHandler(EntityExistsException ex) {

        Span span = tracer.currentSpan();
        String traceId = Optional.ofNullable(span)
                .map(Span::context)
                .map(TraceContext::traceId)
                .orElse("");

        log.error("Entity already exist in db. traceId = " + traceId);

        return new ErrorResponse(
                traceId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFoundExceptionHandler(EntityNotFoundException ex) {

        Span span = tracer.currentSpan();
        String traceId = Optional.ofNullable(span)
                .map(Span::context)
                .map(TraceContext::traceId)
                .orElse("");

        log.error("Entity already exist in db. traceId = " + traceId);

        return new ErrorResponse(
                traceId,
                ex.getMessage(),
                OffsetDateTime.now()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorId = UUID.randomUUID().toString();

        if (log.isErrorEnabled()) {
            log.error("Fields validation exception, errorId = " + errorId);
        }

        final List<Violation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }
}