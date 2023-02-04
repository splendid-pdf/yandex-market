package com.yandex.market.orderservice.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    private LocalDateTime timeStamped;
    private String message;
    private int errorCode;
}