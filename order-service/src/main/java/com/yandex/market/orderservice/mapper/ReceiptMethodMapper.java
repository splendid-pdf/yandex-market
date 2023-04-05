package com.yandex.market.orderservice.mapper;

import com.yandex.market.orderservice.dto.ReceiptMethodPreviewDto;
import com.yandex.market.orderservice.dto.seller.SellerOrderContactPreview;
import com.yandex.market.orderservice.model.ReceiptMethod;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiptMethodMapper {

    ReceiptMethodPreviewDto toReceiptMethodDto(ReceiptMethod receiptMethod);

    SellerOrderContactPreview toSellerOrderContact(ReceiptMethod receiptMethod);
}