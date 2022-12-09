package com.market.shopservice.service;

import com.market.shopservice.impl.IShopSystemService;
import com.market.shopservice.repositories.ShopSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopSystemService implements IShopSystemService {
    private ShopSystemRepository repository;

    public String getAllShopSystems() {
        return "Test ... 123";
    }
}
