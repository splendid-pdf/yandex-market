package com.yandex.market.shopservice.service.shop.impl;

import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemResponseDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.ShopSystemRepository;
import com.yandex.market.shopservice.service.shop.ShopSystemService;
import com.yandex.market.shopservice.util.ShopSystemMapStructMapper;
import com.yandex.market.shopservice.util.ShopSystemMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ShopSystemServiceImpl implements ShopSystemService {
    private final ShopSystemRepository repository;
    private final ShopSystemMapper mapper;

    private final ShopSystemMapStructMapper mapStructMapper;

    public Page<ShopSystemResponseDto> getAllShopSystems(Pageable pageable) {
        Page<ShopSystem> shopSystemPages = repository.findAll(pageable);

        return new PageImpl<>(
                shopSystemPages.getContent()
                        .stream()
                        .map(mapStructMapper::toShopSystemResponse)
                        .collect(Collectors.toList()),
                pageable,
                shopSystemPages.getTotalElements());
    }

    @Transactional
    public UUID createShopSystem(ShopSystemRequestDto dto) {
        ShopSystem shopSystem = mapper.toShopSystemFromRequestDto(dto);
        shopSystem.setExternalId(UUID.randomUUID());
        repository.save(shopSystem);
        return shopSystem.getExternalId();
    }

    public ShopSystemResponseDto getShopSystemDtoByExternalId(UUID externalId) {
        return mapper.toShopSystemResponseDto(getShopSystemByExternalId(externalId));
    }

    public ShopSystem getShopSystemByExternalId(UUID externalId) {
        return repository.findByExternalId(externalId)
                .orElseThrow(() -> {
                            throw new EntityNotFoundException("Organization by given externalId = \"" +
                                    externalId + "\" was not found. Search canceled!");
                        }
                );
    }

    @Transactional
    public void deleteShopSystemByExternalId(UUID externalId) {
        ShopSystem shopSystem = repository
                .findByExternalId(externalId)
                .orElseThrow(() -> {
                            throw new EntityNotFoundException("Organization by given externalId = \"" +
                                    externalId + "\" was not found. Deletion canceled!");
                        }
                );
        shopSystem.setDisabled(true);
    }

    @Transactional
    public void updateShopSystemByExternalId(UUID externalId, ShopSystemRequestDto dto) {
        ShopSystem shopSystem = getShopSystemByExternalId(externalId);
        shopSystem.setName(dto.getName());
        shopSystem.setToken(dto.getToken());
        shopSystem.setSupport(mapper.toSupportFromDto(dto.getSupport()));
        shopSystem.setLegalEntityAddress(mapper.toLocationFromDto(dto.getLegalEntityAddress()));
        shopSystem.setLogoUrl(dto.getLogoUrl());
    }
}
