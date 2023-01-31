package com.yandex.market.shopservice.util;

import com.yandex.market.shopservice.dto.LocationDto;
import com.yandex.market.shopservice.dto.branch.BranchDto;
import com.yandex.market.shopservice.dto.branch.BranchResponseDto;
import com.yandex.market.shopservice.dto.projections.BranchResponseProjection;
import com.yandex.market.shopservice.model.Location;
import com.yandex.market.shopservice.model.branch.Branch;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true)
)
public interface BranchMapper {
//    @Mapping(source = "shopSystem.externalId", target = "shopSystem")
    @Mapping(source = "delivery.branch.externalId", target = "delivery.branch")
    BranchDto toBranchDto(Branch branch);

//    @Mapping(source = "shopSystem", target = "shopSystem.externalId")
    @Mapping(source = "delivery.branch", target = "delivery.branch.externalId")
    Branch toBranch(BranchDto dto);

    @Mapping(target = "shopSystem", ignore = true)
    @Mapping(target = "delivery.branch", ignore = true)
    Branch updateBranch(@MappingTarget Branch entity, BranchDto dto);

    BranchResponseDto toBranchDtoResponse(BranchResponseProjection branchResponseProjection);

    Location toLocationFromDto(LocationDto location);
}
