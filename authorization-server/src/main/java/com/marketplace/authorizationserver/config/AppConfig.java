package com.marketplace.authorizationserver.config;

import com.marketplace.authorizationserver.gateway.SellerAuthClient;
import com.marketplace.authorizationserver.gateway.UserRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AppConfig {

    @Bean
    public UserRestClient userRestClient(@Value("${app.security.user-data-provider-url}") String baseUrl) {
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(WebClient.builder()
                        .baseUrl(baseUrl)
                        .build()))
                .build();
        return proxyFactory.createClient(UserRestClient.class);
    }

    @Bean
    public SellerAuthClient sellerAuthClient(@Value("${app.security.seller-data-provider-url}") String baseUrl) {
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(WebClient.builder()
                        .baseUrl(baseUrl)
                        .build()))
                .build();
        return proxyFactory.createClient(SellerAuthClient.class);
    }
}
