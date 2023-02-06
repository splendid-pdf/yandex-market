package com.yandex.market.uploadservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PresignedUrlUploadRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.yandex.market.uploadservice.config.properties.ObjectStorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckStorageService {

    private final AmazonS3 amazonS3;

    private final ObjectStorageProperties properties;

    @SneakyThrows
    public String updateCheck(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(properties.getBucketName(), file.getName(), file.getInputStream(), metadata);
        return file.getOriginalFilename();
    }

}
