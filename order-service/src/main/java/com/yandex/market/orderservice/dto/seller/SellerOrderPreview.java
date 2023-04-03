package com.yandex.market.orderservice.dto.seller;

import com.yandex.market.orderservice.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record SellerOrderPreview(

        LocalDateTime creationTimestamp,
        OrderStatus orderStatus,
        SellerOrderContactPreview sellerOrderContactPreview,
        List<SellerOrderProductPreview> orderedProductPreviews
) {
}