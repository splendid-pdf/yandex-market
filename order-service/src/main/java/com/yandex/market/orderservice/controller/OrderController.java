package com.yandex.market.orderservice.controller;

import com.yandex.market.orderservice.dto.OrderRequest;
import com.yandex.market.orderservice.dto.OrderResponse;
import com.yandex.market.orderservice.dto.OrderResponsePreview;
import com.yandex.market.orderservice.dto.seller.SellerOrderPreview;
import com.yandex.market.orderservice.model.OrderStatus;
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
import java.util.List;
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

    public static final String STATUS_OK = "200";
    public static final String USER_ID = "userId";
    public static final String SUCCESSFUL_OPERATION = "Successful operation";
    public static final String APPLICATION_JSON = "application/json";
    private final OrderService orderService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{userId}/orders")
    @Operation(operationId = "createOrder", summary = "Create new order for the user")
    @ApiResponse(responseCode = "201", description = SUCCESSFUL_OPERATION,
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = UUID.class)))
    public UUID createOrder(@Parameter(name = "orderRequestDto", description = "Representation of a created order")
                            @RequestBody @Valid OrderRequest orderRequest,
                            @Parameter(name = "userId", description = "User's identifier")
                            @PathVariable(USER_ID) UUID userId) {
        log.info("'createOrder' was called for userId = {} with request = {}", userId, orderRequest);
        return orderService.create(orderRequest, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/orders/{orderId}")
    @Operation(operationId = "getByOrderId", summary = "Get order information by it is external id")
    @ApiResponse(responseCode = STATUS_OK, description = SUCCESSFUL_OPERATION,
            content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = OrderResponse.class)))
    public OrderResponse getByOrderId(
            @Parameter(name = "orderId", description = "Order's identifier")
            @PathVariable("orderId") UUID orderId) {
        log.info("'getByOrderId' was called by orderId = {}", orderId);
        return orderService.getOrderResponseById(orderId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{userId}/orders-previews")
    @Operation(operationId = "getOrderByUserId", summary = "Get user orders by user identifier")
    @ApiResponse(responseCode = STATUS_OK, description = SUCCESSFUL_OPERATION,
            content = @Content(mediaType = APPLICATION_JSON,
                    array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class))))
    public Page<OrderResponsePreview> getOrderByUserId(
            @Parameter(name = USER_ID, description = "User's identifier")
            @PathVariable(USER_ID) UUID userId,
            @PageableDefault(sort = "creationTimestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("'getOrderByUserId' was called for userId = {}", userId);
        return orderService.getOrdersByUserId(userId, pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/sellers/{sellerId}/orders")
    @Operation(operationId = "getOrderBySellerId", summary = "Get seller orders by seller identifier")
    @ApiResponse(responseCode = STATUS_OK, description = SUCCESSFUL_OPERATION,
            content = @Content(mediaType = APPLICATION_JSON,
                    array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class))))
    public List<SellerOrderPreview> getOrderBySellerId(
            @Parameter(name = "sellerId", description = "User's identifier")
            @PathVariable("sellerId") UUID sellerId) {
        log.info("'getOrderBySellerId' was called for sellerId: {}", sellerId);
        return orderService.getOrderPreviewsBySellerId(sellerId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/orders/{orderId}/cancellation")
    @Operation(operationId = "cancelOrder", summary = "Cancel order by it's external id")
    @ApiResponse(responseCode = "204", description = SUCCESSFUL_OPERATION)
    public void cancelOrder(@Parameter(name = "orderId", description = "Order's identifier")
                            @PathVariable("orderId") UUID orderId) {
        log.info("'cancelOrder' was called by orderId: {}", orderId);
        orderService.cancelOrder(orderId);
    }

//    @ResponseStatus(HttpStatus.OK)
//    @PutMapping("/orders/{externalId}")
//    @Operation(operationId = "updateOrder", summary = "Update order by it's external id")
//    @ApiResponse(responseCode = "200", description = SUCCESSFUL_OPERATION)
//    public OrderResponseDto updateOrder(
//            @Parameter(name = "orderRequestDto", description = "Representation of a updated order")
//            @RequestBody @Valid OrderRequestDto orderRequestDto,
//            @Parameter(name = EXTERNAL_ID, description = "Order's identifier")
//            @PathVariable(EXTERNAL_ID) UUID externalId) {
//        log.info("Received a request to update an order: {}", externalId);
//        return orderService.update(orderRequestDto, externalId);
//    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/orders/{orderId}/status")
    @Operation(operationId = "updateOrderStatus", summary = "Update order status by order id")
    @ApiResponse(responseCode = STATUS_OK, description = SUCCESSFUL_OPERATION)
    public OrderResponse updateOrderStatus(
            @Parameter(name = "orderId", description = "Order's identifier")
            @PathVariable("orderId") UUID orderId,
            @Parameter(name = "OrderStatus")
            @RequestBody OrderStatus orderstatus
    ) {
        log.info("'updateOrderStatus' was called by orderId: {}", orderId);
        return orderService.updateOrderStatus(orderId, orderstatus);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/orders/{orderId}/check")
    public ResponseEntity<InputStreamResource> receiveOrderCheck(@PathVariable("orderId") UUID orderId) {
        ByteArrayInputStream byteArrayInputStream = orderService.createCheck(orderId);
        log.info("'receiveOrderCheck' was called by orderId: {}", orderId);

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=check.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(byteArrayInputStream));
    }
}
