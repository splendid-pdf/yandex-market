package com.yandex.market.mapper;

public interface Mapper<T, R> {
    R map(T t);
}
