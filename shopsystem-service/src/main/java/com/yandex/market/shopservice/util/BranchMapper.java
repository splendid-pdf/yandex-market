package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.model.branch.Branch;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface BranchMapper {
    @Mapping(source = "shopSystem.externalId", target = "shopSystem")
    @Mapping(source = "delivery.branch.externalId", target = "delivery.branch")
    BranchDto toBranchDto(Branch branch);

    @Mapping(source = "shopSystem", target = "shopSystem.externalId")
    @Mapping(source = "delivery.branch", target = "delivery.branch.externalId")
    Branch toBranch(BranchDto dto);
}
