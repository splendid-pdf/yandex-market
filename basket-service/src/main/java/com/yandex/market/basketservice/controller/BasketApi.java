package com.yandex.market.basketservice.controller;

import com.yandex.market.basketservice.dto.CountItemsResponse;
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
                description = "На сервер переданы неверные данные",
                content = {
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                }
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Пользователь не авторизован",
                content = {
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                }
        ),
        @ApiResponse(
                responseCode = "403",
                description = "У пользователя нет доступа к ресурсу",
                content = {
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorResponse.class)
                        )
                }
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Ресурс не найден",
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
            summary = "Получить страницу с добавленными товарами в корзину",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Страница со списком товаров в корзине успешно получена"
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
            @Parameter(name = "userId", description = "Идентификатор покупателя", required = true) UUID userId,
            @Parameter(name = "pageable", description = "Информация о пагинации") Pageable pageable
    );

    @Operation(
            summary = "Добавление товара в корзину или изменение количества товара уже добавленного в корзину",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Товар успешно добавлен в корзину с новым значением количества",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CountItemsResponse.class)
                    )
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
    CountItemsResponse changeItemCountInBasket(
            @Parameter(name = "userId", description = "Идентификатор покупателя", required = true) UUID userId,
            @Parameter(name = "itemRequest", description = "Информация о добавляемом товаре", required = true) ItemRequest itemRequest
    );

    @Operation(
            summary = "Удаление одного или несколько товаров из корзины",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Выбранные товары успешно удалены из корзины",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CountItemsResponse.class)
                    )
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
    CountItemsResponse deleteItemsList(
            @Parameter(name = "userId", description = "Идентификатор покупателя", required = true) UUID userId,
            @Parameter(name = "itemIds", description = "Список удаляемых товаров", required = true) List<UUID> itemIds
    );
}
