package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.request.ProductCharacteristicRequest;
import com.yandex.market.productservice.dto.response.ProductCharacteristicResponse;
import com.yandex.market.productservice.model.ProductCharacteristic;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductCharacteristicMapper {

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    ProductCharacteristic toProductCharacteristic(ProductCharacteristicRequest productCharacteristicRequest);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "valueType", ignore = true)
    ProductCharacteristic toProductCharacteristic(ProductCharacteristicRequest productCharacteristicRequest,
                                                  @MappingTarget ProductCharacteristic productCharacteristic);

    @Mapping(target = "id", source = "externalId")
    @Mapping(target = "value", expression = "java(productCharacteristic.getValueType().parse(productCharacteristic.getValue()))")
    ProductCharacteristicResponse toProductCharacteristicDto(ProductCharacteristic productCharacteristic);
}