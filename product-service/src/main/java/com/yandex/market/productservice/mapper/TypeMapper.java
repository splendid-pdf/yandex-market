package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.TypeDto;
import com.yandex.market.productservice.dto.response.TypeResponse;
import com.yandex.market.productservice.model.Type;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true),
        uses = {TypeCharacteristicMapper.class, RoomMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TypeMapper {

    Type toType(TypeDto typeDto);

    TypeDto toTypeDto(Type type);

    @Mapping(source = "externalId", target = "id")
    @Mapping(source = "typeCharacteristics", target = "typeCharacteristicDtos")
    TypeResponse toTypeResponse(Type type);
}