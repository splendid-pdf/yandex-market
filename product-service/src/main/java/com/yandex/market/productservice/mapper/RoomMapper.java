package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.RoomDto;
import com.yandex.market.productservice.model.Room;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    Room toRoom(RoomDto roomDto);

    RoomDto toRoomDto(Room room);
}