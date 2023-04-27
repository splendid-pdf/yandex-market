package com.marketplace.sellerinfoservice.controller;

import com.marketplace.sellerinfoservice.service.SellerService;
import com.yandex.market.auth.dto.ClientAuthDetails;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/private/api/v1/sellers/auth")
public class AuthController {

    private final SellerService sellerService;

    @GetMapping
    public ClientAuthDetails authenticate(@RequestParam String email) {
        return sellerService.getSellerAuthDetails(email);
    }
}