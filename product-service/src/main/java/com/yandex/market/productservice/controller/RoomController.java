package com.yandex.market.productservice.controller;

import com.yandex.market.productservice.controller.response.ErrorResponse;
import com.yandex.market.productservice.dto.projections.RoomPreview;
import com.yandex.market.productservice.dto.response.TypeResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.app.room.url}")
@Tag(name = "API для работы с сущностью Room")
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

    @GetMapping("{roomId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getRoomById", summary = "Получить информацию о комнате по id")
    @ApiResponse(responseCode = "200", description = "Информация успешно получена")
    public RoomPreview getRoomById(
                                    @PathVariable
                                    @Parameter(description = "Идентификатор комнаты")
                                    UUID roomId
    ) {
        return roomService.getRoomById(roomId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getAllRooms", summary = "Получить список всех комнат")
    @ApiResponse(responseCode = "200", description = "Информация успешно получена")
    public Page<RoomPreview> getAllRooms(
                                         @PageableDefault(
                                                 size = 20,
                                                 sort = "name",
                                                 direction = Sort.Direction.DESC
                                         )
                                         Pageable pageable
    ) {
        return roomService.getAllRooms(pageable);
    }

    @GetMapping("types/{typeId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getRoomById", summary = "Получить информацию о комнате по id")
    @ApiResponse(responseCode = "200", description = "Информация успешно получена")
    public Page<RoomPreview> getAllRoomsTypeId(
                                               @PathVariable
                                               @Parameter(description = "Идентификатор типа")
                                               UUID typeId,
                                               @PageableDefault(
                                                       size = 20,
                                                       sort = "name",
                                                       direction = Sort.Direction.DESC
                                               )
                                               Pageable pageable
    ) {
        return roomService.getAllRoomsByTypeId(typeId, pageable);
    }



}
