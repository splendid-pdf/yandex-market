package com.market.shopservice.controllers;

import com.market.shopservice.models.ShopSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/public/api/v1/shopsystem")
public class ShopSystemController {

//    private final ShopSystemService shopService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    private List<ShopSystem> shops (){
//        return shopService.getAllShopSystems();
        return null;
    }
}
