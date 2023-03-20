package com.marketplace.aggregatorservice.dto;

public record ProductCharacteristicDto(

        String name,

        String value,

        ValueType valueType) {
}