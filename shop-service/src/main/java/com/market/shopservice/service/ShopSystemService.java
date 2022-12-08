package com.market.shopservice.service;

import com.market.shopservice.models.ShopSystem;
import com.market.shopservice.repositories.ShopSystemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopSystemService {
    private ShopSystemRepository repository;
//
//    public ShopSystemService(ShopSystemRepository repository) {
//        this.repository = repository;
//    }

    public List<ShopSystem> getAllShopSystems() {
        ShopSystem byId = repository.getById(1L);
        return List.of(byId);
    }
}
