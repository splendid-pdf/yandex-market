package com.yandex.market.uploadservice.controller;

import com.yandex.market.model.ErrorResponse;
import com.yandex.market.uploadservice.model.FileType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@Tag(name = "Upload")
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
                description = "Ресурс не был найден по url",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
public interface UploadApi {

    URL upload(MultipartFile file, FileType fileType);

    List<URL> upload(List<MultipartFile> files, FileType fileType);

    ResponseEntity<byte[]> download(String url);

}
