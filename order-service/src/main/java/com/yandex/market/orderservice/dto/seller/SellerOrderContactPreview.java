package com.yandex.market.orderservice.dto.seller;

import com.yandex.market.orderservice.model.Address;

public record SellerOrderContactPreview(

        String receiverName,
        String receiverPhone,
        Address address
) {
}