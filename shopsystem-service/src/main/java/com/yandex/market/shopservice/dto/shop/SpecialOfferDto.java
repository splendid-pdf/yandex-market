package com.yandex.market.shopservice.dto.shop;

import com.yandex.market.shopservice.model.shop.SpecialOfferType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpecialOfferDto {
    private UUID shopSystem;

    @EqualsAndHashCode.Include
    @NotBlank(message = "\"Name\" field must not be empty")
    private String name;

    @NotNull(message = "\"Type of Special offer\" field must not be empty")
    private SpecialOfferType type;

    @Positive(message = "\"Value of Special Offer\" field must not be negative")
    private int value;

    private String terms;
}
