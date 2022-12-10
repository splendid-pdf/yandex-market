package com.market.shopservice.service;

import com.market.shopservice.impl.IShopSystemService;
import com.market.shopservice.models.shop.ShopSystem;
import com.market.shopservice.repositories.ShopSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopSystemService implements IShopSystemService {
    private final ShopSystemRepository repository;

    public List<ShopSystem> getAllShopSystems() {
        return repository.findAll();
    }

    public void createShopSystem(ShopSystem shopSystem) {
        // TODO: различные проверки

        shopSystem.setExternalId(UUID.randomUUID());
        repository.save(shopSystem);
    }

    public ShopSystem getShopSystemByExternalId(String externalId) {
        return repository.findByExternalId(externalId);
    }

//    public ShopSystem createShopSystem(ShopSystemDao dao) {
//        return repository.save(dao);
//    }
}
