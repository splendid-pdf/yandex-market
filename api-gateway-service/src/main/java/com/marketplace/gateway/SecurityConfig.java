package com.marketplace.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .cors().disable()
                .authorizeExchange()
                .pathMatchers(
                        "/login",
                        "/actuator/**",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui*/**",
                        "/swagger-resources/**",
                        "/public/api/v1/users/signup")
                .permitAll()
                .anyExchange().authenticated()
                .and().oauth2Login()
                .and().build();
    }
}