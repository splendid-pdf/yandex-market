package com.yandex.market.orderservice.controller;

import com.itextpdf.text.DocumentException;
import com.yandex.market.orderservice.dto.OrderPreviewDto;
import com.yandex.market.orderservice.dto.OrderRequestDto;
import com.yandex.market.orderservice.dto.OrderResponseDto;
import com.yandex.market.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

@RestController
@RequestMapping("/public/api/v1")
@RequiredArgsConstructor
@Tag(name = "orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    @Operation(summary = "Create new order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Create new User"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public OrderResponseDto create(@RequestBody @Valid OrderRequestDto orderRequestDto,
                                   @PathVariable("userId") UUID userId) {
        return orderService.create(orderRequestDto, userId);
    }

    @GetMapping("/orders/{externalId}")
    public OrderResponseDto getByExternalId(@PathVariable("externalId") UUID externalId) {
        return orderService.getByExternalId(externalId);
    }

    @GetMapping("/users/{userId}/orders")
    public Page<OrderPreviewDto> getOrderByUserId(
            @PathVariable("userId") UUID userId,
            @PageableDefault(sort = "creationTimestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return orderService.getOrdersByUserId(userId, pageable);
    }

    @PutMapping("/orders/{externalId}/cancelation")
    public String cancelOrder(@PathVariable("externalId") UUID externalId) {
        return orderService.cancelOrder(externalId);
    }

    @PutMapping("/orders/{externalId}")
    public OrderResponseDto updateOrder(@RequestBody @Valid OrderRequestDto orderRequestDto,
                                        @PathVariable("externalId") UUID externalId) {
        return orderService.update(orderRequestDto, externalId);
    }

    @GetMapping("/orders/{externalId}/check")
    public ResponseEntity<InputStreamResource> createCheck(@PathVariable("externalId") UUID externalID) throws DocumentException, FileNotFoundException {
        ByteArrayInputStream byteArrayInputStream = orderService.createReceiptForUser(externalID);
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=check.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }
}