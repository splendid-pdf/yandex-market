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

    @Mapping(source = "externalId", target = "id")
    TypeDto toTypeDto(Type type);

    @Mapping(source = "externalId", target = "id")
    @Mapping(source = "typeCharacteristics", target = "characteristics")
    TypeResponse toTypeResponse(Type type);
}