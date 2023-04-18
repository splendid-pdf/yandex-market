package com.yandex.market.uploadservice.controller;

import com.amazonaws.services.s3.Headers;
import com.yandex.market.uploadservice.model.FileAttributes;
import com.yandex.market.uploadservice.model.FileDetails;
import com.yandex.market.uploadservice.model.FileType;
import com.yandex.market.uploadservice.service.StorageService;
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

import static com.yandex.market.util.HttpUtils.PUBLIC_API_V1;

@Slf4j
@Tag(name = "files")
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
    @ResponseStatus(HttpStatus.OK)
    public URL upload(@RequestPart MultipartFile file) {
        return storageService.uploadFile(file);
    }

    @GetMapping(value = "/files")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> download(@RequestParam("fileId") String fileId,
                                           @RequestParam("fileType") FileType fileType) {
        FileDetails fileDetails = storageService.downloadFile(fileId, fileType);
        HttpHeaders headers = createFileDownloadHeaders(fileDetails);
        return new ResponseEntity<>(fileDetails.getContent(), headers, HttpStatus.OK);
    }

    @DeleteMapping(value = "/files")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam("fileId") String fileId,
                       @RequestParam("fileType") FileType fileType) {
        storageService.deleteFile(fileId, fileType);
    }

//    @GetMapping(value = "/urls")
//    @ResponseStatus(HttpStatus.OK)
//    public Set<URL> getUrlsByIds(
//            @RequestBody List<String> fileIds,
//            @RequestParam("fileType") FileType fileType
//    ) {
//        return storageService.getUrlsByObjectIds(fileIds, fileType);
//    }

    private HttpHeaders createFileDownloadHeaders(FileDetails fileDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(Headers.CONTENT_DISPOSITION, "attachment; filename=" + fileDetails.getFilename());
        headers.add(Headers.CONTENT_TYPE, fileDetails.getContentType());
        return headers;
    }
}