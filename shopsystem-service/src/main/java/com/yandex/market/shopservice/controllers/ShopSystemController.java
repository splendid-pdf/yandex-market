package com.yandex.market.shopservice.controllers;

import com.yandex.market.shopservice.dto.ShopSystemDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.service.ShopSystemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/public/api/v1/shopsystems")
public class ShopSystemController {
    private final ShopSystemService shopService;

    public ShopSystemController(ShopSystemService shopService) {
        this.shopService = shopService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ShopSystem> getAllShopSystems() {
        return shopService.getAllShopSystems();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewShopSystem(@RequestBody ShopSystemDto shopSystemDto) {
        shopService.createShopSystem(shopSystemDto);
    }

    @GetMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public ShopSystem getShopSystemByExternalId(@PathVariable("externalId") UUID externalId) {
        return shopService.getShopSystemByExternalId(externalId);
    }

    @DeleteMapping("/{externalId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSystemShopByExternalId(@PathVariable("externalId") UUID externalId) {
        shopService.deleteSystemShopByExternalId(externalId);
    }
}
