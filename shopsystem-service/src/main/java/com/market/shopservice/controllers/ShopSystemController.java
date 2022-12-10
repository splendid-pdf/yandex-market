package com.market.shopservice.controllers;

import com.market.shopservice.dao.ShopSystemDao;
import com.market.shopservice.models.shop.ShopSystem;
import com.market.shopservice.service.ShopSystemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/api/v1/shopsystem")
public class ShopSystemController {
    private final ShopSystemService shopService;

    public ShopSystemController(ShopSystemService shopService) {
        this.shopService = shopService;
    }

    // TODO: удалить после всех тестов и проверок (не имеет значимости)
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ShopSystem> getAllShopSystems(){
        return shopService.getAllShopSystems();
    }

    // TODO: обновить до принятия DAO
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewShopSystem(@RequestBody ShopSystem shopSystem) {
        shopService.createShopSystem(shopSystem);
    }
}
