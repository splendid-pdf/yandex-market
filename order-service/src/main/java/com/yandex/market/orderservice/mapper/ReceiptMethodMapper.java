package com.yandex.market.orderservice.mapper;

import com.yandex.market.orderservice.dto.ReceiptMethodDto;
import com.yandex.market.orderservice.model.ReceiptMethod;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiptMethodMapper {

    ReceiptMethodDto toReceiptMethodDto(ReceiptMethod receiptMethod);
}
