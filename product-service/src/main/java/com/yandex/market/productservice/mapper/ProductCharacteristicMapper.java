package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.ProductCharacteristicDto;
import com.yandex.market.productservice.dto.request.ProductCharacteristicUpdateDto;
import com.yandex.market.productservice.model.ProductCharacteristic;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductCharacteristicMapper {

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    ProductCharacteristic toProductCharacteristic(ProductCharacteristicDto productCharacteristicDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductCharacteristic toProductCharacteristic(ProductCharacteristicUpdateDto productCharacteristicUpdateDto,
                                                  @MappingTarget ProductCharacteristic productCharacteristic);

    ProductCharacteristicDto toProductCharacteristicDto(ProductCharacteristic productCharacteristic);
}
