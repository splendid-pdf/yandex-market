package com.yandex.market.userinfoservice.specification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Filter {
    //for cast
    private Class<? extends Comparable> aClass;
    private String field;
    private QueryOperations operator;
    private Object value;
}