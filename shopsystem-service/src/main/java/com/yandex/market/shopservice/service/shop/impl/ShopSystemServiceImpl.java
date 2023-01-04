package com.yandex.market.shopservice.service.shop.impl;

import com.yandex.market.shopservice.dto.shop.ShopSystemBranchInfoDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemRequestDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemResponsesDto;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.ShopSystemRepository;
import com.yandex.market.shopservice.service.shop.ShopSystemService;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopSystemServiceImpl implements ShopSystemService {
    private final ShopSystemRepository repository;
    private final ShopSystemMapper mapper;

    @Override
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
    public UUID createShopSystem(ShopSystemRequestDto dto) {
        ShopSystem shopSystem = mapper.toShopSystemFromRequestDto(dto);
        shopSystem.setExternalId(UUID.randomUUID());
        repository.save(shopSystem);
        return shopSystem.getExternalId();
    }

    @Override
    public ShopSystemResponsesDto getShopSystemDtoByExternalId(UUID externalId) {
        return mapper.toShopSystemResponseDto(getShopSystemByExternalId(externalId));
    }

    @Override
    public ShopSystemBranchInfoDto getShopSystemInfoForBranch(UUID externalId) {
        ShopSystem shopSystem = getShopSystemByExternalId(externalId);
        return ShopSystemBranchInfoDto.builder()
                .shopSystemExternalId(shopSystem.getExternalId())
                .companyName(shopSystem.getName())
                .companyLogoUrl(shopSystem.getLogoUrl())
                .build();
    }

    @Override
    public ShopSystem getShopSystemByExternalId(UUID externalId) {
        return repository.findByExternalId(externalId)
                .orElseThrow(() -> {
                            throw new EntityNotFoundException("Organization by given externalId = \"" +
                                    externalId + "\" was not found. Search canceled!");
                        }
                );
    }

    @Override
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

    @Override
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
