package com.yandex.market.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptMethodDto {

    private LocalDate deliveryDate;

    private LocalTime deliveryStart;

    private LocalTime deliveryEnd;
}
