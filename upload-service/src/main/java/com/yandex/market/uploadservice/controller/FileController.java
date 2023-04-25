package com.yandex.market.uploadservice.controller;

import com.yandex.market.uploadservice.model.FileType;
import com.yandex.market.uploadservice.service.StorageService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@RestController
@RequestMapping(PUBLIC_API_V1)
@RequiredArgsConstructor
public class FileController {

    private final StorageService storageService;

    @PostMapping(
            value = "/files",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(value = " @permissionService.hasPermission()")
    public List<UUID> upload(@RequestPart @Size(min = 1, max = 10) List<MultipartFile> files,
                             @RequestParam FileType fileType) {
        return storageService.uploadFiles(files, fileType);
    }

    @GetMapping("/files")
    @ResponseStatus(HttpStatus.OK)
    public List<URL> getUrls(@RequestParam List<UUID> filesIds) {
        return storageService.getUrls(filesIds);
    }

}