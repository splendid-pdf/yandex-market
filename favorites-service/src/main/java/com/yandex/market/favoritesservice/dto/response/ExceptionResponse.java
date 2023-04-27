package com.yandex.market.favoritesservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Schema
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    private UUID exceptionId;

    private LocalDateTime timeStamped;

    private String message;

    private String debugMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;
}