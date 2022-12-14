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
    private final ShopSystemMapper mapper = new ShopSystemMapper();

    public Page<ShopSystemDto> getAllShopSystems(Pageable pageable) {
        Page<ShopSystem> shopSystemPages = repository.findAll(pageable);
        return new PageImpl<>(
                mapper.toShopSystemDtoPages(shopSystemPages.getContent()),
                pageable,
                shopSystemPages.getTotalElements());
    }

    @Transactional
    public void createShopSystem(ShopSystemDto dto) {
        ShopSystem shopSystem = mapper.toShopSystemFromDto(dto);
        shopSystem.setExternalId(UUID.randomUUID());
        repository.save(shopSystem);
    }

    public ShopSystem getShopSystemByExternalId(UUID externalId) {
        return repository
                .findByExternalId(externalId)
                .orElseThrow(() -> {
                            log.error("REQUEST REJECTED. Could not find a matching record.");
                            throw new EntityNotFoundException("Organization by given externalId = \"" +
                                    externalId + "\" was not found. Creation canceled!");
                        }
                );
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
    }
}
