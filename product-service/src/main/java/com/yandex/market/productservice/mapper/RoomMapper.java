package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.response.RoomResponse;
import com.yandex.market.productservice.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    @Mapping(target = "id", source = "externalId")
    RoomResponse toRoomResponse(Room room);

}