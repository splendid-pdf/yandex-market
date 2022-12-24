package com.yandex.market.shopservice.service.branch;

import com.yandex.market.shopservice.dto.branch.BranchDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BranchService {

    UUID createBranch(BranchDto dto);

    BranchDto getBranchDtoByExternalId(UUID externalId);

    void updateBranchByExternalId(UUID externalId, BranchDto dto);

    Page<BranchDto> getAllBranchesByShopSystem(UUID externalId);
}
