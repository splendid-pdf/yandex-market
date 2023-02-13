package com.yandex.market.uploadservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.yandex.market.exception.BadRequestException;
import com.yandex.market.uploadservice.config.properties.ObjectStorageProperties;
import com.yandex.market.uploadservice.model.DownloadFileInfo;
import com.yandex.market.uploadservice.model.FileType;
import com.yandex.market.uploadservice.validator.FileValidator;
import jakarta.activation.MimetypesFileTypeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yandex.market.uploadservice.utils.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final AmazonS3 amazonS3;
    private final ObjectStorageProperties properties;

    private final FileValidator validator;

    @Value("${application.validation.maximum_files_count}")
    private Integer maxFilesCount;

    private final static String EXTENSION = "extension";

    public String uploadFile(MultipartFile file, String fileId, FileType fileType) {
        validator.validate(file);
        try {
            val bucketName = properties.getBucketName();
            val objectId = createObjectId(fileId, fileType);
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
            throw new BadRequestException(UPLOAD_FILE_EXCEPTION_MESSAGE.formatted(file.getName()));
        }
    }

    public URL getFileUrlById(String fileId, FileType fileType) {
        try {
            val bucketName = properties.getBucketName();
            val objectId = createObjectId(fileId, fileType);
            val expirationTime = new DateTime().plusMinutes(properties.getExpirationTime()).toDate();
            if (FileType.CHECK == fileType) {
                return amazonS3.generatePresignedUrl(
                        bucketName,
                        objectId,
                        expirationTime
                );
            }
            return amazonS3.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, objectId));
        } catch (AmazonS3Exception exception) {
            log.error("Failed to retrieve a file by fileId = {}", fileId);
            throw new BadRequestException(GET_FILE_URL_EXCEPTION_MESSAGE.formatted(fileId));
        }
    }

    public DownloadFileInfo downloadFile(String fileId, FileType fileType) {
        val objectId = createObjectId(fileId, fileType);
        val information = new DownloadFileInfo();
        val s3Object = amazonS3.getObject(properties.getBucketName(), objectId);

        information.setFilename(fileId + getFileExtensionByS3Object(s3Object));
        information.setContentType(new MimetypesFileTypeMap().getContentType(information.getFilename()));

        try {
            information.setContent(s3Object.getObjectContent().readAllBytes());
        } catch (IOException e) {
            log.error("Failed to download a file by key = {}", objectId);
            throw new BadRequestException(DOWNLOAD_FILE_EXCEPTION_MESSAGE.formatted(objectId));
        }
        return information;
    }

    public void deleteFile(String fileId, FileType fileType) {
        val objectId = createObjectId(fileId, fileType);
        amazonS3.deleteObject(
                properties.getBucketName(),
                objectId
        );
    }

    public Set<URL> getUrlsByObjectIds(List<String> fileIds, FileType fileType) {
        checkListIfSizeGreaterThanMaxFilesCount(fileIds);
        return fileIds
                .stream()
                .map(fileId -> getFileUrlById(fileId, fileType))
                .collect(Collectors.toSet());
    }

    private String createObjectId(String fileId, FileType fileType) {
        return fileType.getFolder() + fileId;
    }

    private String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    private ObjectMetadata createMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(Map.of(EXTENSION, "." + getExtension(file.getOriginalFilename())));
        return metadata;
    }

    private static String getFileExtensionByS3Object(S3Object s3Object) {
        return s3Object.getObjectMetadata().getUserMetadata().get(EXTENSION);
    }

    private void checkListIfSizeGreaterThanMaxFilesCount(List<String> fileIds) {
        if (fileIds.size() > maxFilesCount) {
            throw new IllegalArgumentException(
                    NUMBER_OF_FILES_UPLOADED_EXCEEDED_EXCEPTION_MESSAGE + maxFilesCount
            );
        }
    }
}