package com.yandex.market.shopservice.service;

import com.yandex.market.shopservice.dto.ShopSystemDto;
import com.yandex.market.shopservice.extensions.ShopSystemNotFound;
import com.yandex.market.shopservice.impl.IShopSystemService;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.ShopSystemRepository;
import com.yandex.market.shopservice.util.ShopSystemUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopSystemService implements IShopSystemService {
    private final ShopSystemRepository repository;
    private final ShopSystemUtil util;

    public List<ShopSystem> getAllShopSystems() {
        // предполагается ли, что нам надо возвращать удалённые организации?
        return repository.findAll();
    }

    @Transactional
    public void createShopSystem(ShopSystemDto dto) {
        // Предполагается что если до сюда дошёл класс, то он прошёл все проверки и может быть добавлен
        dto.setExternalId(UUID.randomUUID());
        repository.save(util.convertDtoToShopSystem(dto));
    }

    public ShopSystem getShopSystemByExternalId(UUID externalId) {
        return repository
                .findByExternalId(externalId)
                .orElseThrow(() -> new ShopSystemNotFound("Organization by given externalId = \"" +
                        externalId + "\" was not found. Creation canceled!")
                );
    }

    @Transactional
    public void deleteSystemShopByExternalId(UUID externalId) {
        ShopSystem shopSystem = repository
                .findByExternalId(externalId)
                .orElseThrow(() -> new ShopSystemNotFound("Organization by given externalId = \"" +
                        externalId + "\" was not found. Deletion canceled!")
                );
        shopSystem.setDisabled(true);
    }
}
