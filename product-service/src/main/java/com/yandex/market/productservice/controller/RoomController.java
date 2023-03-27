package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.controller.response.ErrorResponse;
import com.yandex.market.productservice.dto.projections.RoomPreview;
import com.yandex.market.productservice.service.RoomService;
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
@Tag(name = "Room")
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
                description = "Комната не найдена",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/rooms/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getRoomPreviewById", summary = "Получить комнату по ее id")
    @ApiResponse(responseCode = "200", description = "Комната успешно получена")
    public RoomPreview getRoomPreviewById(
                                    @PathVariable
                                    @Parameter(description = "Идентификатор комнаты")
                                    UUID roomId
    ) {

        return roomService.getRoomPreviewById(roomId);
    }

    @GetMapping("/rooms")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getRoomPreviews", summary = "Получить отсортированный список всех комнат")
    @ApiResponse(responseCode = "200", description = "Отсортированный список комнат успешно получен")
    public List<RoomPreview> getRoomPreviews() {
        return roomService.getRoomPreviews();
    }

    @GetMapping("/types/{typeId}/rooms")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getRoomPreviewsByTypeId", summary = "Получить отсортированный  список комнат которые есть у переданного типа товаров")
    @ApiResponse(responseCode = "200", description = "Отсортированный список комнат успешно получен")
    public List<RoomPreview> getRoomPreviewsByTypeId(
                                               @PathVariable
                                               @Parameter(description = "Идентификатор типа товара")
                                               UUID typeId
    ) {
        return roomService.getRoomPreviewsByTypeId(typeId);
    }
}