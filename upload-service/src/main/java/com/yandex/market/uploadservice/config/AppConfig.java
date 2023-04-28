package com.yandex.market.uploadservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class AppConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludePayload(false);
        filter.setIncludeClientInfo(true);
        filter.setIncludeQueryString(true);
        filter.setMaxPayloadLength(100);
        filter.setIncludeHeaders(false);
        filter.setBeforeMessagePrefix("REQUEST: ");
        filter.setAfterMessagePrefix("RESPONSE: ");
        return filter;
    }

}