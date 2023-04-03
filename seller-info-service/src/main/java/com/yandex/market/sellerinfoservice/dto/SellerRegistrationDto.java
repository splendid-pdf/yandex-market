package com.yandex.market.sellerinfoservice.dto;

import com.yandex.market.sellerinfoservice.validator.annotation.SellerEmailConstraint;
import com.yandex.market.sellerinfoservice.validator.annotation.SellerPasswordConstraint;
public record SellerRegistrationDto(

        @SellerEmailConstraint
        String email,
        @SellerPasswordConstraint
        String password
) {
}