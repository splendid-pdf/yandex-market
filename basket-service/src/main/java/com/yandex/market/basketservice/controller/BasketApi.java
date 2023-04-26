package com.yandex.market.basketservice.controller;

import com.yandex.market.basketservice.dto.ItemRequest;
import com.yandex.market.basketservice.dto.ItemResponse;
import com.yandex.market.model.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@Tag(name = "baskets")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Invalid data provided to the server",
                content = {
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                }
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized error"
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden to get a resource"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Not found requested resource",
                content = {
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                }
        )
})
public interface BasketApi {

    @Operation(
            summary = "Get the page with items which were added to the basket by the user",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ItemResponse.class)
                            )
                    }
            )
    )
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #userId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    Page<ItemResponse> getAllItemsInsideBasketByUserId(
            @Parameter(name = "id", description = "user external ID", required = true) UUID userId,
            @Parameter(name = "pageable", description = "pageable") Pageable pageable
    );


    @Operation(
            summary = "Adding an item to the basket and changing the count of the item",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(mediaType = "application/json")
            )
    )
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #userId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    Integer changeItemCountInBasket(
            @Parameter(name = "userId", description = "user external ID", required = true) UUID userId,
            @Parameter(name = "Item request", description = "Information about added item", required = true) ItemRequest itemRequest
    );

    @Operation(
            description = "delete one or more items from the basket",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(mediaType = "application/json")
            )
    )
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #userId,
                T(com.yandex.market.auth.model.Role).USER,
                T(com.yandex.market.auth.util.ClientAttributes).USER_ID
            )
            """
    )
    Integer deleteItemsList(
            @Parameter(name = "id", description = "user external id", required = true) UUID userId,
            @Parameter(name = "itemsIds", description = "list items for deleting", required = true) List<UUID> itemsIdsList
    );

}
