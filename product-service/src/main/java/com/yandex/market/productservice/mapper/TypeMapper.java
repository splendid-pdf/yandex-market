package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.TypeDto;
import com.yandex.market.productservice.model.Type;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CharacteristicMapper.class, RoomMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface TypeMapper {

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(source = "typeDto.typeCharacteristicDto", target = "typeCharacteristics")
    @Mapping(source = "typeDto.roomDto", target = "rooms")
    Type toType(TypeDto typeDto);

    TypeDto toTypeDto(Type type);
}