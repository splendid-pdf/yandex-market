package com.yandex.market.productservice.dto;

import java.util.Objects;

public record ProductCountDto(String productId, Long count) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCountDto that = (ProductCountDto) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
