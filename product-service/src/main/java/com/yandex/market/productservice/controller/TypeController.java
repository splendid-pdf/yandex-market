package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.controller.response.ErrorResponse;
import com.yandex.market.productservice.dto.projections.TypePreview;
import com.yandex.market.productservice.dto.response.TypeResponse;
import com.yandex.market.productservice.service.TypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(PUBLIC_API_V1)
@Tag(name = "Type")
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
                responseCode = "404",
                description = "Тип товара не найден",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
public class TypeController {

    private final TypeService typeService;

    @GetMapping("/types/{typeId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getType", summary = "Получить тип по его id")
    @ApiResponse(responseCode = "200", description = "Тип товара успешно получен")
    public TypeResponse getTypeById(
                                    @PathVariable
                                    @Parameter(description = "Идентификатор типа товара")
                                    UUID typeId
    ) {
        return typeService.getTypeById(typeId);
    }

    @GetMapping("/types")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getAllTypes", summary = "Получить список всех типов товаров")
    @ApiResponse(responseCode = "200", description = "Список типов успешно получен")
    public List<TypePreview> getAllTypes(
                                         @PageableDefault(
                                                 size = Integer.MAX_VALUE,
                                                 sort = "name",
                                                 direction = Sort.Direction.DESC)
                                         Pageable pageable
    ) {
        return typeService.getAllTypes(pageable);
    }

    @GetMapping("rooms/{roomId}/types")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getAllTypesByRoomId", summary = "Получить список типов товаров принадлежащих комнате")
    @ApiResponse(responseCode = "200", description = "Список типов товаров успешно получен")
    public List<TypePreview> getAllTypesByRoomId(
                                                 @PathVariable
                                                 @Parameter(description = "Идентификатор комнаты")
                                                 UUID roomId,
                                                 @PageableDefault(
                                                         size = Integer.MAX_VALUE,
                                                         sort = "name",
                                                         direction = Sort.Direction.DESC)
                                                 Pageable pageable

    ) {
        return typeService.getAllTypesByRoomId(roomId, pageable);
    }
}