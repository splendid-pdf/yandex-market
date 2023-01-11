package com.yandex.market.shopservice.service.branch.impl;

import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.dto.branch.BranchResponseDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemBranchInfoDto;
import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.branch.Delivery;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.model.shop.Support;
import com.yandex.market.shopservice.repositories.BranchRepository;
import com.yandex.market.shopservice.service.branch.BranchService;
import com.yandex.market.shopservice.service.shop.ShopSystemService;
import com.yandex.market.shopservice.util.ShopSystemMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BranchServiceImpl implements BranchService {
    private final BranchRepository repository;
    private final ShopSystemService shopSystemService;
    private final ShopSystemMapper mapper;

    @Override
    public UUID createBranch(BranchDto dto) {
        Branch branch = mapper.toBranchFromDto(dto);
        branch.setExternalId(UUID.randomUUID());
        ShopSystem shopSystem = shopSystemService.getShopSystemByExternalId(dto.getShopSystem());
        Delivery delivery = mapper.toDeliveryFromDto(dto.getDelivery());
        branch.setDelivery(delivery);
        shopSystem.addBranch(branch);
        repository.save(branch);
        return branch.getExternalId();
    }

    private Branch getBranchByExternalId(UUID externalId) {
        return repository.findByExternalId(externalId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Branch by given externalId = \"" +
                            externalId + "\" was not found. Search canceled!");
                });
    }


    @Override
    public BranchResponseDto getBranchResponseDtoByExternalId(UUID externalId) {
        Branch branch = getBranchByExternalId(externalId);
        ShopSystem shopSystem = shopSystemService.getShopSystemByExternalId(branch.getShopSystem().getExternalId());
        ShopSystemBranchInfoDto shopSystemInfoForBranch = shopSystemService
                .getShopSystemInfoForBranch(shopSystem);
        Support support = shopSystem.getSupport();
        return mapper.toBranchDtoResponse(branch, shopSystemInfoForBranch, support);
    }


    @Override
    @Transactional
    public void updateBranchByExternalId(UUID externalId, BranchDto dto) {
        Branch branch = getBranchByExternalId(externalId);
        branch.setName(dto.getName());
        branch.setToken(dto.getToken());
        branch.setOgrn(dto.getOgrn());
        branch.setLocation(mapper.toLocationFromDto(dto.getLocation()));
        branch.setContact(mapper.tocContactFromDto(dto.getContact()));
        branch.setDelivery(mapper.toDeliveryFromDto(dto.getDelivery()));
    }

    @Override
    public Page<BranchResponseDto> getBranchesByShopSystem(UUID externalId, Pageable pageable) {
        return getBranchPageImpl(pageable,
                shopSystemService.getShopSystemByExternalId(externalId),
                repository.findAllByShopSystemExternalId(externalId, pageable));
    }

    private PageImpl<BranchResponseDto> getBranchPageImpl(
            Pageable pageable, ShopSystem shopSystem, Page<Branch> branches) {
        return new PageImpl<>(
                branches.getContent().stream()
                        .map(br -> mapper.toBranchDtoResponse(br,
                                shopSystemService.getShopSystemInfoForBranch(shopSystem),
                                shopSystem.getSupport()))
                        .collect(Collectors.toList()),
                pageable,
                branches.getTotalElements()
        );
    }
}
