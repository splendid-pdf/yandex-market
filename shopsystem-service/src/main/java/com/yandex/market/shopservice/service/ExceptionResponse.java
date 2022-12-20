package com.yandex.market.shopservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    private LocalDateTime timeStamped;
    private String message;
    private String debugMessage;
    private int errorCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;
}