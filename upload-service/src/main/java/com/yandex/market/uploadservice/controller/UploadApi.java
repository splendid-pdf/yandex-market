package com.yandex.market.uploadservice.controller;

import com.yandex.market.model.ErrorResponse;
import com.yandex.market.uploadservice.model.FileType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Tag(name = "Upload")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "На сервер переданны неверные данные",
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
                responseCode = "404",
                description = "Данные не были найдены по переданным url",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
public interface UploadApi {

    @Operation(operationId = "upload", summary = "Загрузить файлы на сервер", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "Файлы успешно загружены", content = @Content(mediaType = "application/json"))
    @PreAuthorize(value = "@permissionService.hasPermission()")
    List<UUID> upload(List<MultipartFile> files, FileType fileType);

    @Operation(operationId = "getUrls", summary = "Получить ссылки на фотографии")
    @ApiResponse(responseCode = "200", description = "Ссылки успешно получены", content = @Content(mediaType = "application/json"))
    List<URL> getUrls(List<UUID> filesIds);

}
