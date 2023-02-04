package com.yandex.market.orderservice.controller;

import com.yandex.market.orderservice.dto.OrderPreviewDto;
import com.yandex.market.orderservice.dto.OrderRequestDto;
import com.yandex.market.orderservice.dto.OrderResponseDto;
import com.yandex.market.orderservice.repository.OrderRepository;
import com.yandex.market.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@RestController
@RequestMapping(PUBLIC_API_V1)
@RequiredArgsConstructor
@Tag(name = "orders")
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Invalid data provided to the server",
                content = @Content(mediaType = "application/json"))})
public class OrderController {

    private final OrderService orderService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{userId}/orders")
    @Operation(operationId = "createOrder", summary = "Create new order for the user")
    @ApiResponse(responseCode = "201", description = "Successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class)))
    public UUID createOrder(@Parameter(name = "orderRequestDto", description = "Representation of a created order")
                            @RequestBody @Valid OrderRequestDto orderRequestDto,
                            @Parameter(name = "userId", description = "User's identifier")
                                @PathVariable("userId") UUID userId) {
        log.info("Received a request to create new order %s for user: %s" .formatted(orderRequestDto, userId));
        return orderService.create(orderRequestDto, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/orders/{externalId}")
    @Operation(operationId = "getByExternalId", summary = "Get order information by it is external id")
    @ApiResponse(responseCode = "200", description = "Successful operation", content =
    @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class)))
    public OrderResponseDto getByExternalId(
            @Parameter(name = "externalId", description = "Order's identifier")
                @PathVariable("externalId") UUID externalId) {
        log.info("Received a request to get orders by order identifier: %s" .formatted(externalId));
        return orderService.getOrderResponseDtoByExternalId(externalId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{userId}/orders")
    @Operation(operationId = "getOrderByUserId", summary = "Get user orders by user identifier")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class))))
    public Page<OrderPreviewDto> getOrderByUserId(
            @Parameter(name = "userId", description = "User's identifier")
            @PathVariable("userId") UUID userId,
            @PageableDefault(sort = "creationTimestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Received a request to get orders by user identifier: %s" .formatted(userId));
        return orderService.getOrdersByUserId(userId, pageable);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/orders/{externalId}/cancellation")
    @Operation(operationId = "cancelOrder", summary = "Cancel order by it's external id")
    @ApiResponse(responseCode = "204", description = "Successful operation")
    public void cancelOrder(@Parameter(name = "externalId", description = "Order's identifier")
                                @PathVariable("externalId") UUID externalId) {
        log.info("Received a request to cancel an order: %s" .formatted(externalId));
        orderService.cancelOrder(externalId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/orders/{externalId}")
    @Operation(operationId = "updateOrder", summary = "Update order by it's external id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public OrderResponseDto updateOrder(
            @Parameter(name = "orderRequestDto", description = "Representation of a updated order")
            @RequestBody @Valid OrderRequestDto orderRequestDto,
            @Parameter(name = "externalId", description = "Order's identifier")
                @PathVariable("externalId") UUID externalId) {
        log.info("Received a request to update an order: %s" .formatted(externalId));
        return orderService.update(orderRequestDto, externalId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/orders/{externalId}/check")
    public ResponseEntity<InputStreamResource> receiveOrderCheck(@PathVariable("externalId") UUID externalID) {
        ByteArrayInputStream byteArrayInputStream = orderService.createCheck(externalID);
        log.info("Received a request to generate of check of order: %s" .formatted(externalID));

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=check.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }
}