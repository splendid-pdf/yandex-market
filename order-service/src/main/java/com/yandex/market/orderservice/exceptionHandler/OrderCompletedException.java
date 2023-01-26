package com.yandex.market.orderservice.exceptionHandler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class OrderCompletedException extends RuntimeException {

    public OrderCompletedException(String message) {
        super(message);
    }
}