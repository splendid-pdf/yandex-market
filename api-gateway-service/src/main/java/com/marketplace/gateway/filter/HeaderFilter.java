package com.marketplace.gateway.filter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

import static com.marketplace.gateway.util.AuthConstants.X_USER_ID_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class HeaderFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);
        String userIdHeader = exchange.getRequest().getHeaders().getFirst(X_USER_ID_HEADER);

        if (!exchange.getRequest().getPath().value().contains("logout")) {
            HttpHeaders headers = HttpHeaders.writableHttpHeaders(exchange.getResponse().getHeaders());
            if (StringUtils.hasText(authorizationHeader)) {
                headers.addIfAbsent(AUTHORIZATION, authorizationHeader);
            }
            if (StringUtils.hasText(userIdHeader)) {
                headers.addIfAbsent(X_USER_ID_HEADER, userIdHeader);
            }

            setHeaders(headers, exchange);
        }

        return chain.filter(exchange);
    }

    @SneakyThrows
    private  void setHeaders(HttpHeaders headers, ServerWebExchange serverWebExchange) {
        Field headersField = serverWebExchange.getResponse()
                .getClass()
                .getSuperclass()
                .getDeclaredField("headers");
        headersField.setAccessible(true);
        headersField.set(serverWebExchange.getResponse(), headers);
    }
}