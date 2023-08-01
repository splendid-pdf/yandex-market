package com.marketplace.workflow.core.operations;

import com.marketplace.workflow.dto.ProductCountDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class SendOrderOperation extends Operation{
    private final String orderId;
    private final String sellerId;
    private final List<ProductCountDto> productCountDtoList;
    private final String userId;
}
