package com.yandex.market.orderservice.controller;

import com.yandex.market.orderservice.dto.OrderDto;
import com.yandex.market.orderservice.service.OrderService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/public/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/users/{userId}")
    public OrderDto create(@RequestBody OrderDto orderDto, @PathVariable("userId") UUID userId) {
        return orderService.create(orderDto, userId);
    }

    @GetMapping("/{externalId}")
    public OrderDto getByExternalId(@PathVariable("externalId") UUID externalId) {
        return orderService.getByExternalId(externalId);
    }

    @GetMapping("/users/{userId}")
    public List<OrderDto> getOrderByUserId(@PathVariable("userId") UUID userId,
                                           @RequestParam("page") @Min(0) int page,
                                           @RequestParam("size") @Min(1) int size) {
        return orderService.getOrderByUserId(userId, page, size);
    }

    @GetMapping("/privet")
    public String privet() {
        return "privet";
    }
}