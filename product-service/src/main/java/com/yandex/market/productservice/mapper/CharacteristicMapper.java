package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.response.TypeCharacteristicResponse;
import com.yandex.market.productservice.model.TypeCharacteristic;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CharacteristicMapper {

    TypeCharacteristic toCharacteristic(TypeCharacteristicResponse typeCharacteristicResponse);

    TypeCharacteristicResponse toCharacteristicDto(TypeCharacteristic typeCharacteristic);

}