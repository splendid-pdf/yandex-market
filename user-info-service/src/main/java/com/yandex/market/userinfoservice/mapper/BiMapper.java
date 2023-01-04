package com.yandex.market.userinfoservice.mapper;

public interface BiMapper<RS, RQ, M> {
    M map(RQ rq);

    RS mapToResponseDto(M m);
}
