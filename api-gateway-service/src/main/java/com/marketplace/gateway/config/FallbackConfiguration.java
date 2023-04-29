package com.marketplace.gateway.config;

import com.yandex.market.model.ErrorResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@Configuration
public class FallbackConfiguration {

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions
                .route(RequestPredicates.path("/fallback"), this::handleFallback);
    }

    private Mono<ServerResponse> handleFallback(ServerRequest request) {
        return ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Mono.fromSupplier(() ->
                      new ErrorResponse(
                              UUID.randomUUID().toString(),
                              "Service currently unavailable",
                              OffsetDateTime.now())),
                        ErrorResponse.class
                );
    }
}
