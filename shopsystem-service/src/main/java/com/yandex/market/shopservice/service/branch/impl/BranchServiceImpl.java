package com.yandex.market.shopservice.service.branch.impl;

import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.dto.branch.BranchResponseDto;
import com.yandex.market.shopservice.dto.shop.ShopSystemBranchInfoDto;
import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.shop.ShopSystem;
import com.yandex.market.shopservice.repositories.BranchRepository;
import com.yandex.market.shopservice.service.branch.BranchService;
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
public class BranchServiceImpl implements BranchService {
    private final BranchRepository repository;
    private final ShopSystemService shopSystemService;
    private final ShopSystemMapper mapper;

    @Override
    @Transactional
    public UUID createBranch(BranchDto dto) {
        Branch branch = mapper.toBranchFromDto(dto);
        branch.setExternalId(UUID.randomUUID());
        ShopSystem shopSystem = shopSystemService.getShopSystemByExternalId(dto.getShopSystem());
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
        ShopSystemBranchInfoDto shopSystem = shopSystemService
                .getShopSystemInfoForBranch(branch.getShopSystem().getExternalId());
        return mapper.toBranchDtoResponse(branch, shopSystem);
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
        ShopSystemBranchInfoDto shopSystem = shopSystemService.getShopSystemInfoForBranch(externalId);
        Page<Branch> branches = repository.findAllByShopSystemExternalId(externalId, pageable);
        return new PageImpl<>(
                branches.getContent().stream()
                        .map(br -> mapper.toBranchDtoResponse(br, shopSystem))
                        .collect(Collectors.toList()),
                pageable,
                branches.getTotalElements()
        );
    }
}
