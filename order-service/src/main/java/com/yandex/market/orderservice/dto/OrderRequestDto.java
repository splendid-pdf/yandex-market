package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.OrderedProduct;
import com.yandex.market.orderservice.model.PaymentType;
import com.yandex.market.orderservice.model.ReceiptMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    private PaymentType paymentType;

    private double price;

    private boolean paid;

    private LocalDateTime paymentDateTime;

    private LocalDateTime creationTimestamp;

    private ReceiptMethod receiptMethod;

    private List<OrderedProduct> orderedProducts;
}