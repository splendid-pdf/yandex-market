package com.marketplace.sellerinfoservice.dto;

import com.marketplace.sellerinfoservice.validator.annotation.SellerEmailConstraint;
import com.marketplace.sellerinfoservice.validator.annotation.SellerPasswordConstraint;
public record SellerRegistration(
        @SellerEmailConstraint
        String email,
        @SellerPasswordConstraint
        String password
) {
}