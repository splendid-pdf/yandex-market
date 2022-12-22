package com.yandex.market.shopservice.service.branch.impl;

import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.repositories.BranchRepository;
import com.yandex.market.shopservice.service.branch.BranchService;
import com.yandex.market.shopservice.util.ShopSystemServiceMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository repository;
    private final ShopSystemServiceMapper mapper;

    @Override
    @Transactional
    public void createBranch(BranchDto dto) {
        Branch branch = mapper.toBranchFromDto(dto);
        branch.setExternalId(UUID.randomUUID());
        // TODO нужно записать бренч в систем шоп
        repository.save(branch);
    }
}
