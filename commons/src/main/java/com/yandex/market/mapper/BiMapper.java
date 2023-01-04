package com.yandex.market.mapper;

public interface BiMapper <T1, T2, R> {
    R map (T1 t1, T2 t2);
}