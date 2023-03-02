package com.yandex.market.sellerinfoservice.mapper;

import com.yandex.market.sellerinfoservice.dto.SellerRequestDto;
import com.yandex.market.sellerinfoservice.dto.SellerResponseDto;
import com.yandex.market.sellerinfoservice.model.Seller;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SellerMapper {
    SellerResponseDto toSellerResponseDto(Seller seller);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", expression = "java(UUID.randomUUID())")
    Seller toSellerModel(SellerRequestDto sellerRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Seller updateSellerModel(SellerRequestDto sellerRequestDTO, @MappingTarget Seller seller);
}
