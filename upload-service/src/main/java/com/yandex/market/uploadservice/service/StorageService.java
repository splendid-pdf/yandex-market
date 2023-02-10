package com.yandex.market.uploadservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yandex.market.exception.BadRequestException;
import com.yandex.market.uploadservice.config.properties.ObjectStorageProperties;
import com.yandex.market.uploadservice.model.FileType;
import com.yandex.market.uploadservice.validator.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final AmazonS3 amazonS3;
    private final ObjectStorageProperties properties;

    private final FileValidator validator;

    public String uploadFile(MultipartFile file, String fileId, FileType type) {
        validator.validate(file);
        try {
            val bucketName = properties.getBucketName();
            val objectId = type.getFolder() + fileId;
            val inputStream = file.getInputStream();
            val metadata = createMetadata(file);
            amazonS3.putObject(
                    bucketName,
                    objectId,
                    inputStream,
                    metadata
            );

            return objectId;
        } catch (IOException e) {
            log.error("Failed to upload a file = {}", file);
            throw new BadRequestException("Unable to upload a file = %s".formatted(file.getName()));
        }
    }

    public URL getFileUrlById(String fileId, FileType fileType) {
        try {
            val bucketName = properties.getBucketName();
            val objectId = fileType.getFolder() + fileId;
            val expirationTime = new DateTime().plusMinutes(properties.getExpirationTime()).toDate();
            return amazonS3.generatePresignedUrl(
                    bucketName,
                    objectId,
                    expirationTime
            );
        } catch (AmazonS3Exception exception) {
            log.error("Failed to retrieve a file by fileId = {}", fileId);
            throw new BadRequestException("Unable to find a file by fileId = %s".formatted(fileId));
        }
    }

    public byte[] downloadFile(String fileId, FileType fileType) {
        val objectId = fileType.getFolder() + fileId;

        try {
            return amazonS3
                    .getObject(properties.getBucketName(), objectId)
                    .getObjectContent()
                    .readAllBytes();
        } catch (IOException e) {
            log.error("Failed to download a file by key = {}", objectId);
            throw new BadRequestException("Unable to find a file by key = %s".formatted(objectId));
        }
    }

    public void deleteFile(String fileId, FileType fileType) {
        val objectId = fileType.getFolder() + fileId;
        amazonS3.deleteObject(
                properties.getBucketName(),
                objectId
        );
    }

    private ObjectMetadata createMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }


    // TODO: Получение множества урлов файлов по ObjectIds.
    // TODO: Загрузка множества файлов (опред File0Type) return множество Ids; (не больше опред количества файлов)(config).
    // TODO: Валидации на размеры файлов (для каждого типа файла свои ограничения).
    // TODO: Expiration убрать для картинок, добавить для чеков например.
    // TODO: Получение множества файлов по ObjectIds (1 тип).
    // TODO: Добывать расширение файла
    // TODO: ACL настройка файла
    // TODO: Удаление файла
}
