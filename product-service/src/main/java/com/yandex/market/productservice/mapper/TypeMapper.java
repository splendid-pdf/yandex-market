package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.TypeDto;
import com.yandex.market.productservice.model.Type;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CharacteristicMapper.class, RoomMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TypeMapper {

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    Type toType(TypeDto typeDto);

    TypeDto toTypeDto(Type type);
}