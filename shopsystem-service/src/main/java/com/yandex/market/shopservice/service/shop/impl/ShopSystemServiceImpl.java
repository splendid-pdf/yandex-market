package com.yandex.market.shopservice.service.shop.impl;

import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemResponsesDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.ShopSystemRepository;
import com.yandex.market.shopservice.service.shop.ShopSystemService;
import com.yandex.market.shopservice.util.ShopSystemServiceMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopSystemServiceImpl implements ShopSystemService {
    private final ShopSystemRepository repository;
    private final ShopSystemServiceMapper mapper;

    public Page<ShopSystemResponsesDto> getAllShopSystems(Pageable pageable) {
        Page<ShopSystem> shopSystemPages = repository.findAll(pageable);
        return new PageImpl<>(
                shopSystemPages.getContent()
                        .stream()
                        .map(mapper::toShopSystemResponseDto)
                        .collect(Collectors.toList()),
                pageable,
                shopSystemPages.getTotalElements());
    }

    @Transactional
    public void createShopSystem(ShopSystemRequestDto dto) {
        ShopSystem shopSystem = mapper.toShopSystemFromRequestDto(dto);
        shopSystem.setExternalId(UUID.randomUUID());
        repository.save(shopSystem);
    }

    public ShopSystemResponsesDto getShopSystemDtoByExternalId(UUID externalId) {
        return mapper.toShopSystemResponseDto(
                repository.findByExternalId(externalId)
                        .orElseThrow(() -> {
                                    log.error("REQUEST REJECTED. Could not find a matching record.");
                                    throw new EntityNotFoundException("Organization by given externalId = \"" +
                                            externalId + "\" was not found. Search canceled!");
                                }
                        )
        );
    }

    @Override
    public ShopSystem getShopSystemByExternalId(UUID externalId) {
        return repository.findByExternalId(externalId)
                .orElseThrow(() -> {
                            log.error("REQUEST REJECTED. Could not find a matching record.");
                            throw new EntityNotFoundException("Organization by given externalId = \"" +
                                    externalId + "\" was not found. Search canceled!");
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

    @Transactional
    public void updateSystemShopByExternalId(UUID externalId, ShopSystemRequestDto dto) {
        ShopSystem shopSystem = mapper.toShopSystemFromResponseDto(getShopSystemDtoByExternalId(externalId));
        shopSystem.setName(dto.getName());
        shopSystem.setToken(dto.getToken());
        shopSystem.setSupport(mapper.toSupportFromDto(dto.getSupport()));
        shopSystem.setLegalEntityAddress(mapper.toLocationFromDto(dto.getLegalEntityAddress()));
        shopSystem.setLogoUrl(dto.getLogoUrl());
    }
}
