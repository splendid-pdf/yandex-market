package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.TypeCharacteristicDto;
import com.yandex.market.productservice.dto.TypeDto;
import com.yandex.market.productservice.model.Type;
import com.yandex.market.productservice.model.TypeCharacteristic;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TypeCharacteristicMapper {

    TypeCharacteristic toTypeCharacteristic(TypeCharacteristicDto typeCharacteristicDto);

    TypeCharacteristicDto toDto(TypeCharacteristic typeCharacteristic);

}
