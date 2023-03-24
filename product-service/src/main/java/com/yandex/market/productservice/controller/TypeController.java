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
                description = "�� ������ �������� �������� ������",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "��� �� ������",
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
    @Operation(operationId = "getType", summary = "�������� ���������� � ����� �� id")
    @ApiResponse(responseCode = "200", description = "���������� ������� ��������")
    public TypeResponse getTypeById(
                                    @PathVariable
                                    @Parameter(description = "������������� ����")
                                    UUID typeId
    ) {
        return typeService.getTypeById(typeId);
    }

    @GetMapping("/types")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getAllTypes", summary = "�������� ������ ��� ���� �����")
    @ApiResponse(responseCode = "200", description = "���������� ������� ��������")
    public Page<TypePreview> getAllTypes(
                                         @PageableDefault(
                                                 size = 20,
                                                 sort = "name",
                                                 direction = Sort.Direction.DESC)
                                         Pageable pageable
    ) {
        return typeService.getAllTypes(pageable);
    }

    @GetMapping("rooms/{roomId}/types")
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "getAllTypesByRoomId", summary = "�������� ������ ��� ���� ����� ������������� ������� (���������)")
    @ApiResponse(responseCode = "200", description = "���������� ������� ��������")
    public Page<TypePreview> getAllTypesByRoomId(
                                                 @PathVariable
                                                 @Parameter(description = "������������� ������� (���������)")
                                                 UUID roomId,
                                                 @PageableDefault(
                                                         size = 20,
                                                         sort = "name",
                                                         direction = Sort.Direction.DESC)
                                                 Pageable pageable

    ) {
        return typeService.getAllTypesByRoomId(roomId, pageable);
    }



}
