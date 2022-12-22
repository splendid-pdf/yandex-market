package com.yandex.market.shopservice.service.branch;

import com.yandex.market.shopservice.dto.branch.BranchDto;

import java.util.UUID;

public interface BranchService {

    UUID createBranch(BranchDto dto);
}
