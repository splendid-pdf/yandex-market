package com.marketplace.workflow.core.operations;

import com.marketplace.workflow.dto.ProductCountDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class ChangeStatusOrderOperation extends Operation{
    private final String orderId;
    private final String sellerId;
    private final Set<ProductCountDto> productCountDtoList;
}
