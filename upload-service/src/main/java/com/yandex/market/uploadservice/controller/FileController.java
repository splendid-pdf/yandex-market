package com.yandex.market.uploadservice.controller;

import com.amazonaws.services.s3.Headers;
import com.yandex.market.uploadservice.model.FileType;
import com.yandex.market.uploadservice.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Slf4j
@RestController
@RequestMapping("public/api/v1")
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;

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
        log.info("Uploading a file with name = {}", file.getOriginalFilename());
        return storageService.uploadFile(file, fileId, fileType);
    }

    @GetMapping(value = "/getFileUrlById")
    @ResponseStatus(HttpStatus.OK)
    public URL getFileUrlById(
            @RequestParam("fileId") String fileId,
            @RequestParam("fileType") FileType fileType
    ) {
        log.info("Getting a file url with name = {}", fileId);
        return storageService.getFileUrlById(fileId, fileType);
    }

    @GetMapping(value = "/download")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> download(
            @RequestParam("fileId") String fileId,
            @RequestParam("fileType") FileType fileType
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Headers.CONTENT_DISPOSITION, "attachment; filename=" + fileId);
        headers.add("Content-Type", fileType.getMediaType());
        log.info("Downloading a file with name = {}", fileId);
        return new ResponseEntity<>(storageService.downloadFile(fileId, fileType), headers, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @RequestParam("fileId") String fileId,
            @RequestParam("fileType") FileType fileType
    ) {
       log.info("Deleting a file with name = {}", fileId);
       storageService.deleteFile(fileId, fileType);
    }
}