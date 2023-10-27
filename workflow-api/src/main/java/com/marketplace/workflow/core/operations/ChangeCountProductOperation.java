package com.marketplace.workflow.core.operations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
//@Component
@RequiredArgsConstructor
public class ChangeCountProductOperation extends Operation{
    private final String sellerId;
    private final String productId;
    private final Long count;
}
