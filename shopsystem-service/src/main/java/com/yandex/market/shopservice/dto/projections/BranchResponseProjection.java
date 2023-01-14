package com.yandex.market.shopservice.dto.projections;

import com.yandex.market.shopservice.model.branch.Branch;
import com.yandex.market.shopservice.model.shop.ShopSystem;

public interface BranchResponseProjection {
    Branch getBranch();

    ShopSystem getShopSystem();
}
