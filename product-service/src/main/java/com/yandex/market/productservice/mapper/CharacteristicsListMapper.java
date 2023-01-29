package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.CharacteristicDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CharacteristicMapper.class)
public interface CharacteristicsListMapper {
    List<CharacteristicDto> toDtoList(List<Charachteristic> charachteristicsList);
    List<Charachteristic> toCharachteristicsList(List<CharacteristicDto> characteristicDto);
}
