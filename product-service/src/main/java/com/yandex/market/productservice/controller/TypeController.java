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
    @Operation(operationId = "getTypeById", summary = "Получить тип по его id")
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
    @Operation(operationId = "getTypePreviews", summary = "Получить список всех типов товаров")
    @ApiResponse(responseCode = "200", description = "Список типов успешно получен")
    public List<TypePreview> getTypePreviews() {
        return typeService.getTypePreviews();
    }

    @GetMapping("rooms/{roomId}/types")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getTypePreviewsByRoomId", summary = "Получить список типов товаров принадлежащих комнате")
    @ApiResponse(responseCode = "200", description = "Список типов товаров успешно получен")
    public List<TypePreview> getTypePreviewsByRoomId(
                                                 @PathVariable
                                                 @Parameter(description = "Идентификатор комнаты")
                                                 UUID roomId) {
        return typeService.getTypePreviewsByRoomId(roomId);
    }
}