package com.yandex.market.productservice.controller;

import com.yandex.market.model.ErrorResponse;
import com.yandex.market.productservice.dto.ProductImageDto;
import com.yandex.market.productservice.dto.ProductRepresentationSetDto;
import com.yandex.market.productservice.dto.projections.SellerProductPreview;
import com.yandex.market.productservice.dto.projections.UserProductPreview;
import com.yandex.market.productservice.dto.request.CreateProductRequest;
import com.yandex.market.productservice.dto.request.ProductCharacteristicRequest;
import com.yandex.market.productservice.dto.request.ProductUpdateRequest;
import com.yandex.market.productservice.dto.request.SpecialPriceRequest;
import com.yandex.market.productservice.dto.response.ProductCharacteristicResponse;
import com.yandex.market.productservice.dto.response.ProductResponse;
import com.yandex.market.productservice.dto.response.SpecialPriceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@Tag(name = "Products")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "На сервер переданы неверные данные",
                content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Пользователь не авторизован",
                content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "У пользователя нет доступа к ресурсу",
                content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Ресурс не был найден по id",
                content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
public interface ProductApi {

    @Operation(operationId = "createProduct", summary = "Создание нового товара", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Продукт успешно создан", content = @Content(mediaType = "application/json"))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            """
    )
    UUID createProduct(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "createProductRequest", description = "Данные необходимые для создания товара",
                    required = true)
            CreateProductRequest createProductRequest
    );

    @Operation(operationId = "getProductById", summary = "Получение товара по id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Товар успешно получен", content = @Content(mediaType = "application/json"))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            and
            @securityService.hasAccessToProduct(#sellerId, #productId)
            """
    )
    ProductResponse getProductById(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "productId", description = "Идентификатор товара", required = true)
            UUID productId
    );

    @Operation(operationId = "updateProductById", summary = "Обновление товара по id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Товар успешно обновлен", content = @Content(mediaType = "application/json"))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            and
            @securityService.hasAccessToProduct(#sellerId, #productId)
            """
    )
    ProductResponse updateProductById(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "productId", description = "Идентификатор товара", required = true)
            UUID productId,
            @Parameter(name = "productUpdateRequest", description = "Данные об обновлении товара", required = true)
            ProductUpdateRequest productUpdateRequest
    );

    @Operation(operationId = "deleteProducts", summary = "Удаление списка товаров", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Товары успешно удалены")
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )           
            """
    )
    void deleteProductsList(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "productIds", description = "Список идентификаторов товаров", required = true)
            List<UUID> productIds
    );

    @Operation(operationId = "addImage", summary = "Добавить изображение товара", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Изображение товара успешно добавлено", content = @Content(mediaType = "application/json"))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            and
            @securityService.hasAccessToProduct(#sellerId, #productId)
            """
    )
    ProductImageDto addImage(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "productId", description = "Идентификатор товара", required = true)
            UUID productId,
            @Parameter(name = "productImageDto", description = "Данные об изображении товара", required = true)
            ProductImageDto productImageDto
    );

    @Operation(operationId = "deleteImage", summary = "Удалить изображение товара", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Изображение успешно удалено")
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            and
            @securityService.hasAccessToImage(#sellerId, #url)
            """
    )
    void deleteImageByUrl(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "url", description = "Ссылка на изображение товара", required = true)
            String url
    );

    @Operation(operationId = "createSpecialPrice", summary = "Добавить акцию", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Акция успешно добавлена", content = @Content(mediaType = "application/json"))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            and
            @securityService.hasAccessToProduct(#sellerId, #productId)
            """
    )
    UUID createSpecialPriceByProductId(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "productId", description = "Идентификатор товара", required = true)
            UUID productId,
            @Parameter(name = "specialPriceRequest", description = "Данные об акции", required = true)
            SpecialPriceRequest specialPriceRequest
    );

    @Operation(operationId = "updateSpecialPrice", summary = "Обновить акцию", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Акция успешно обновлена", content = @Content(mediaType = "application/json"))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            and
            @securityService.hasAccessToSpecialPrice(#sellerId, #specialPriceId)
            """
    )
    SpecialPriceResponse updateSpecialPriceById(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "specialPriceId", description = "Идентификатор акции", required = true)
            UUID specialPriceId,
            @Parameter(name = "specialPriceRequest", description = "Данные об акции", required = true)
            SpecialPriceRequest specialPriceRequest
    );

    @Operation(operationId = "deleteSpecialPrice", summary = "Удалить акцию", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Акция успешно удалена")
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            and
            @securityService.hasAccessToSpecialPrice(#sellerId, #specialPriceId)
            """
    )
    void deleteSpecialPriceById(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "specialPriceId", description = "Идентификатор акции", required = true)
            UUID specialPriceId
    );

    @Operation(operationId = "updateProductCharacteristic", summary = "Обновить характеристику товара", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Характеристика товара успешно обновлена", content = @Content(mediaType = "application/json"))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            and
            @securityService.hasAccessToCharacteristic(#sellerId, #characteristicId)
            """
    )
    ProductCharacteristicResponse updateProductCharacteristicById(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "characteristicId", description = "Идентификатор характеристики", required = true)
            UUID characteristicId,
            @Parameter(name = "productCharacteristicRequest", description = "Обновленные данные характеристики",
                       required = true)
            ProductCharacteristicRequest productCharacteristicRequest
    );

    @Operation(operationId = "getProductPreviewsBySellerId", summary = "Получить страницу с краткой информацией о товарах", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Страница с краткой информацией о товарах успешно получена", content = @Content(mediaType = "application/json"))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            """
    )
    Page<SellerProductPreview> getProductPreviewsBySellerId(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "pageable", description = "pageable")
            Pageable pageable
    );

    @Operation(operationId = "getArchivedProductPreviewsBySellerId", summary = "Получить страницу с краткой информацией о товарах в архиве", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Страница с краткой информацией о товарах в архиве успешно получена", content = @Content(mediaType = "application/json"))
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            """
    )
    Page<SellerProductPreview> getArchivedProductPreviewsBySellerId(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "pageable", description = "pageable")
            Pageable pageable
    );

    @Operation(summary = "Добавить продукты в архив", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Товары успешно добавлены в архив")
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            """
    )
    void changeIsArchiveField(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "isArchive", description = "Архивировать ли товары", required = true)
            boolean isArchive,
            @Parameter(name = "productIds", description = "Список идентификаторов товаров", required = true)
            List<UUID> productIds
    );

    @Operation(summary = "Изменить видимость товаров для покупателей", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Видимость товаров было успешно изменена")
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            """
    )
    void changeProductsVisibility(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "isVisible", description = "Видимость товаров", required = true)
            boolean isVisible,
            @Parameter(name = "productIds", description = "Список идентификаторов товаров", required = true)
            List<UUID> productIds
    );

    @Operation(operationId = "changeProductPrice", summary = "Изменить цену товара", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Цена товара была успешно изменена")
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            and
            @securityService.hasAccessToProduct(#sellerId, #productId)
            """
    )
    void changeProductPriceById(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "productId", description = "Идентификатор товара", required = true)
            UUID productId,
            @Parameter(name = "price", description = "Новая цена товара", required = true)
            Long price
    );

    @Operation(operationId = "changeProductPrice", summary = "Изменить количество товара", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Количество товара было успешно изменено")
    @PreAuthorize(value = """
            @permissionService.hasPermission(
                #sellerId,
                T(com.yandex.market.auth.model.Role).SELLER,
                T(com.yandex.market.auth.util.ClientAttributes).SELLER_ID
            )
            and
            @securityService.hasAccessToProduct(#sellerId, #productId)
            """
    )
    void changeProductCountById(
            @Parameter(name = "sellerId", description = "Идентификатор продавца", required = true)
            UUID sellerId,
            @Parameter(name = "productId", description = "Идентификатор товара", required = true)
            UUID productId,
            @Parameter(name = "price", description = "Новая цена товара", required = true)
            Long price
    );

    @Operation(method = "getProductsPreviews",
            description = "Получение превью всех товаров с сортировкой по убыванию времени создания")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    Page<UserProductPreview> getAllProductPreviews(
            Pageable pageable
    );

    @Operation(method = "getNewestProductPreviewsByIdentifiers", description = "Получение превью самых новых товаров")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    List<UserProductPreview> getProductPreviewsByIdentifiers(
            ProductRepresentationSetDto productRepresentationSetDto
    );





}
