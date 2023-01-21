package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponseOrderDto {

    private UUID externalId;

    private OrderStatus orderStatus;

    private double price;

    private boolean paid;

    private LocalDateTime creationTimestamp;

    private ReceiptMethodDto receiptMethodDto;

    private List<OrderedProductResponseDto> orderedProductsResponseDto;
}