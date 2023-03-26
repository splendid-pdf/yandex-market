package com.marketplace.authorizationserver.gateway;

import com.yandex.market.auth.dto.ClientAuthDetails;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface UserRestClient {

    @GetExchange( "/users/auth")
    ClientAuthDetails receiveUserAuthDetails(@RequestParam String email);
}
