package com.yandex.market.orderservice.dto;

import com.yandex.market.orderservice.model.OrderStatus;
import com.yandex.market.orderservice.model.PaymentType;
import com.yandex.market.orderservice.model.OrderedProduct;
import com.yandex.market.orderservice.model.ReceiptMethod;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@Data
public class OrderDto {

    private UUID externalId;

    private double price;

    private OrderStatus orderStatus;

    private List<OrderedProduct> orderedProducts;

    private PaymentType paymentType;

    private ReceiptMethod receiptMethod;

    private boolean paid;

    private LocalDateTime paymentDateTime;

    private LocalDateTime creationTimestamp;
}