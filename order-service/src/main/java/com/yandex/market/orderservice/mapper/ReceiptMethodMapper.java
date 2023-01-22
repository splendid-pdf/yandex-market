package com.yandex.market.orderservice.mapper;

import com.yandex.market.orderservice.dto.ReceiptMethodResponseDto;
import com.yandex.market.orderservice.model.ReceiptMethod;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiptMethodMapper {

    ReceiptMethodResponseDto toReceiptMethodDto(ReceiptMethod receiptMethod);
}
