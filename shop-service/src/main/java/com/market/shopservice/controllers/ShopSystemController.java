package com.market.shopservice.controllers;

import com.market.shopservice.service.ShopSystemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/api/v1/shopsystem")
public class ShopSystemController {
    private final ShopSystemService shopService;

    public ShopSystemController(ShopSystemService shopService) {
        this.shopService = shopService;
    }

}
