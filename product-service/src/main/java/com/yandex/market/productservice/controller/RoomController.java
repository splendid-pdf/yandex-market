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

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(PUBLIC_API_V1)
@Tag(name = "Room")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "�� ������ �������� �������� ������",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "������� �� �������",
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
    @Operation(operationId = "getRoomById", summary = "�������� ���������� � ������� �� id")
    @ApiResponse(responseCode = "200", description = "���������� ������� ��������")
    public RoomPreview getRoomById(
                                    @PathVariable
                                    @Parameter(description = "������������� �������")
                                    UUID roomId
    ) {

        return roomService.getRoomById(roomId);
    }

    @GetMapping("/rooms")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getAllRooms", summary = "�������� ������ ���� ������")
    @ApiResponse(responseCode = "200", description = "���������� ������� ��������")
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

    @GetMapping("/types/{typeId}/rooms")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getRoomById", summary = "�������� ���������� � ������� �� id")
    @ApiResponse(responseCode = "200", description = "���������� ������� ��������")
    public Page<RoomPreview> getAllRoomsTypeId(
                                               @PathVariable
                                               @Parameter(description = "������������� ����")
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