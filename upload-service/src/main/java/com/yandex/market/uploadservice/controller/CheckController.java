package com.yandex.market.uploadservice.controller;

import com.yandex.market.uploadservice.service.CheckStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("public/api/v1/")
@RequiredArgsConstructor
public class CheckController {

    private final CheckStorageService checkStorageService;

    @PostMapping(
            value = "upload/checks",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String uploadCheck(@RequestPart MultipartFile file) {
        log.info("Uploading a check with name = {}", file.getOriginalFilename());
        return checkStorageService.updateCheck(file);
    }
}
