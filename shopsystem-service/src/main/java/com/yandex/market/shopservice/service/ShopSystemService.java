package com.yandex.market.shopservice.service;

import com.yandex.market.shopservice.dto.ShopSystemDto;
import com.yandex.market.shopservice.impl.IShopSystemService;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.ShopSystemRepository;
import com.yandex.market.shopservice.util.ShopSystemMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ShopSystemService implements IShopSystemService {
    private final ShopSystemRepository repository;

    public Page<ShopSystemDto> getAllShopSystems(Pageable pageable) {
        Page<ShopSystem> shopSystemPages = repository.findAll(pageable);
        if (shopSystemPages.getTotalElements() != 0) {
            log.info("All ShopSystem entries are searched.");
        } else {
            log.info("No entries were found.");
            return null;
        }
        return new PageImpl<>(
                ShopSystemMapper.INSTANCE.toPageShopSystemDto(shopSystemPages.getContent()),
                pageable,
                shopSystemPages.getTotalElements());
    }

    @Transactional
    public void createShopSystem(ShopSystemDto dto) {
        ShopSystem shopSystem = ShopSystemMapper.INSTANCE.toShopSystem(dto);
        System.out.println(shopSystem);
        shopSystem.setExternalId(UUID.randomUUID());
        repository.save(shopSystem);
        log.info("REQUEST SUCCESSFUL. ShopSystem = " + shopSystem);
    }

    public ShopSystem getShopSystemByExternalId(UUID externalId) {
        ShopSystem shopSystem = repository
                .findByExternalId(externalId)
                .orElseThrow(() -> {
                            log.error("REQUEST REJECTED. Could not find a matching record.");
                            throw new EntityNotFoundException("Organization by given externalId = \"" +
                                    externalId + "\" was not found. Creation canceled!");
                        }
                );
        log.info("REQUEST SUCCESSFUL. Request received to search for ShopSystem = " + shopSystem.toString());
        return shopSystem;
    }

    @Transactional
    public void deleteSystemShopByExternalId(UUID externalId) {
        ShopSystem shopSystem = repository
                .findByExternalId(externalId)
                .orElseThrow(() -> {
                            log.error("REQUEST REJECTED. Could not find a matching record.");
                            throw new EntityNotFoundException("Organization by given externalId = \"" +
                                    externalId + "\" was not found. Deletion canceled!");
                        }
                );
        shopSystem.setDisabled(true);
        log.info("REQUEST SUCCESSFUL. ShopSystem = \"" + shopSystem.getName() + "\" successfully marked as deleted");
    }
}
