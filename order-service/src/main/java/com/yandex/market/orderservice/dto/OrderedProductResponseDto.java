package com.yandex.market.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProductResponseDto {

    private UUID productId;

    private double amount;

    private String name;

    private String photoUrl;
}