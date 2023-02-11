package com.yandex.market.uploadservice.controller;

import com.amazonaws.services.s3.Headers;
import com.yandex.market.uploadservice.model.FileInformation;
import com.yandex.market.uploadservice.model.FileType;
import com.yandex.market.uploadservice.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.Set;

@Slf4j
@Tag(name = "files")
@RestController
@RequestMapping("public/api/v1")
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;

    @Operation(operationId = "upload", summary = "Upload file")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content())
    @PostMapping(
            value = "/upload",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public String upload(
            @RequestPart MultipartFile file,
            @RequestParam("fileId") String fileId,
            @RequestParam("fileType") FileType fileType
    ) {
        return storageService.uploadFile(file, fileId, fileType);
    }

    @GetMapping(value = "/url")
    @ResponseStatus(HttpStatus.OK)
    public URL getFileUrlById(
            @RequestParam("fileId") String fileId,
            @RequestParam("fileType") FileType fileType
    ) {
        return storageService.getFileUrlById(fileId, fileType);
    }

    @GetMapping(value = "/download")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> download(
            @RequestParam("fileId") String fileId,
            @RequestParam("fileType") FileType fileType
    ) {
        FileInformation fileInformation = storageService.downloadFile(fileId, fileType);
        HttpHeaders headers = new HttpHeaders();
        headers.add(Headers.CONTENT_DISPOSITION, "attachment; filename=" + fileInformation.getFilename());
        headers.add(Headers.CONTENT_TYPE, fileType.getMediaType());
        return new ResponseEntity<>(fileInformation.getContent(), headers, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @RequestParam("fileId") String fileId,
            @RequestParam("fileType") FileType fileType
    ) {
        storageService.deleteFile(fileId, fileType);
    }

    @GetMapping(value = "/urls")
    @ResponseStatus(HttpStatus.OK)
    public Set<URL> getUrlsByIds(
            @RequestBody List<String> fileIds,
            @RequestParam("fileType") FileType fileType
    ) {
        return storageService.getUrlsByObjectIds(fileIds, fileType);
    }

//    @GetMapping(value = "/objectIds")
//    @ResponseStatus(HttpStatus.OK)
//    public List<String> getMultipleIdsByFileIdsAndType(
//            @RequestBody List<String> fileIds,
//            @RequestParam("fileType") FileType fileType
//    ) {
//        log.info("Getting multiple ids from a list of fileIds");
//        return storageService.getObjectIdsByFileIdsAndType(fileIds, fileType);
//    }
}