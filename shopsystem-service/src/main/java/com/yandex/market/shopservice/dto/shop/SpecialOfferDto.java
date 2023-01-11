package com.yandex.market.shopservice.dto.shop;

import com.yandex.market.shopservice.model.shop.SpecialOfferType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.Objects;
import java.util.UUID;

@Builder
public record SpecialOfferDto(
//        @NotNull(message = "\"UUID of Shop System\" field must not be empty")
        UUID shopSystem,

        @NotBlank(message = "\"Name\" field must not be empty")
        String name,

        @NotNull(message = "\"Type of Special offer\" field must not be empty")
        SpecialOfferType type,

        @Positive(message = "\"Value of Special Offer\" field must not be negative")
        int value,

        String terms) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialOfferDto that = (SpecialOfferDto) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
