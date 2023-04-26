package com.marketplace.sellerinfoservice.mapper;

import com.marketplace.sellerinfoservice.model.Seller;
import com.marketplace.sellerinfoservice.dto.SellerRegistration;
import com.marketplace.sellerinfoservice.dto.SellerRequestDto;
import com.marketplace.sellerinfoservice.dto.SellerResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SellerMapper {
    SellerResponseDto toSellerResponseDto(Seller seller);

    // TODO: изменить маппер согласно новому DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Seller toSellerModel(SellerRequestDto sellerRequestDTO);

    // TODO: изменить маппер согласно новому DTO
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "deleted",  ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Seller updateSellerModel(SellerRequestDto sellerRequestDTO, @MappingTarget Seller seller);

    @Mapping(target = "role", constant = "SELLER")
    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    Seller toSeller(SellerRegistration sellerRegistration);
}