package com.marketplace.aggregatorservice.dto;

import java.util.Set;


public record TypeDto(

        String name,

        Set<TypeCharacteristicDto> typeCharacteristicDto,

        Set<RoomDto> roomDto) {
}