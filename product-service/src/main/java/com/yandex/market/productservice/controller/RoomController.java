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
    @Operation(operationId = "getRoomById", summary = "Получить комнату по ее id")
    @ApiResponse(responseCode = "200", description = "Комната успешно получена")
    public RoomPreview getRoomById(
                                    @PathVariable
                                    @Parameter(description = "Идентификатор комнаты")
                                    UUID roomId
    ) {

        return roomService.getRoomById(roomId);
    }

    @GetMapping("/rooms")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getAllRooms", summary = "Получить список всех комнат")
    @ApiResponse(responseCode = "200", description = "Список комнат успешно получен")
    public List<RoomPreview> getAllRooms(
                                         @PageableDefault(
                                                 size = Integer.MAX_VALUE,
                                                 sort = "name",
                                                 direction = Sort.Direction.ASC
                                         )
                                         Pageable pageable
    ) {
        return roomService.getAllRooms(pageable);
    }

    @GetMapping("/types/{typeId}/rooms")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getRoomById", summary = "Получить список комнат которые есть у переданного типа товаров")
    @ApiResponse(responseCode = "200", description = "Список комнат успешно получен")
    public List<RoomPreview> getAllRoomsTypeId(
                                               @PathVariable
                                               @Parameter(description = "Идентификатор типа товара")
                                               UUID typeId,
                                               @PageableDefault(
                                                       size = Integer.MAX_VALUE,
                                                       sort = "name",
                                                       direction = Sort.Direction.ASC
                                               )
                                               Pageable pageable
    ) {
        return roomService.getAllRoomsByTypeId(typeId, pageable);
    }
}