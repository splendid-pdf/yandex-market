package com.yandex.market.sellerinfoservice.dto;

import com.yandex.market.sellerinfoservice.validator.annotation.SellerEmailConstraint;
import com.yandex.market.sellerinfoservice.validator.annotation.SellerPasswordConstraint;
public record SellerRegistration(

        @SellerEmailConstraint
        String email,
        @SellerPasswordConstraint
        String password
) {
}