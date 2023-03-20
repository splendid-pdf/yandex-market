package com.marketplace.aggregatorservice.dto;

import java.time.LocalDateTime;


public record ProductSpecialPriceDto(

        LocalDateTime specialPriceFromDate,

        LocalDateTime specialPriceToDate,

        Long specialPrice) {
}