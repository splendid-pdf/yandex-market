package com.yandex.market.shopservice.service.branch;

import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.model.branch.Branch;

import java.util.UUID;

public interface BranchService {

    UUID createBranch(BranchDto dto);

    Branch getBranchByExternalId(UUID externalId);
    void updateBranchByExternalId(UUID externalId, BranchDto dto);
}
