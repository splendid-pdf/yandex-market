package com.yandex.market.productservice.mapper;

import com.yandex.market.productservice.dto.DimensionsDto;
import com.yandex.market.productservice.model.Dimensions;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface DimensionsMapper {
    DimensionsDto toDto(Dimensions dimensions);
    Dimensions toDimensions(DimensionsDto dimensionsDto);
}
