package com.yandex.market.uploadservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.hash.Hashing;
import com.yandex.market.exception.BadRequestException;
import com.yandex.market.uploadservice.config.properties.ObjectStorageProperties;
import com.yandex.market.uploadservice.model.FileAttributes;
import com.yandex.market.uploadservice.model.FileDetails;
import com.yandex.market.uploadservice.model.FileMetaInfo;
import com.yandex.market.uploadservice.model.FileType;
import com.yandex.market.uploadservice.repository.FileMetaInfoRepository;
import com.yandex.market.uploadservice.validator.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static com.yandex.market.uploadservice.utils.Constants.DOWNLOAD_FILE_EXCEPTION_MESSAGE;
import static com.yandex.market.uploadservice.utils.Constants.UPLOAD_FILE_EXCEPTION_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final static String EXTENSION = "extension";

    private final AmazonS3 amazonS3;
    private final FileValidator validator;
    private final FileMetaInfoRepository repository;
    private final ObjectStorageProperties properties;
//    @Value("${application.validation.maximum_files_count}")
//    private Integer maxFilesCount;

    public FileAttributes uploadFile(MultipartFile file, FileType fileType) {
        validator.validate(file);
        try {
            val bucketName = properties.getBucketName();
            val inputStream = file.getInputStream();
            val metadata = createMetadata(file);
            val hash = getHash(file);

            Optional<FileMetaInfo> fileMetaInfo = repository.findByHash(hash);

            if (fileMetaInfo.isEmpty()) {
                val fileName = createFileName(hash);
                val objectId = createObjectId(fileName, fileType);
                val url = generateUrl(fileType, bucketName, objectId).toString();

                val info = repository.save(
                        createFileMetaInfo(hash, url, fileName)
                );

                amazonS3.putObject(
                        bucketName,
                        objectId,
                        inputStream,
                        metadata
                );

                return new FileAttributes(info.getId(), new URL(info.getUrl()));
            }

            return new FileAttributes(fileMetaInfo.get().getFileName(), new URL(fileMetaInfo.get().getUrl()));
        } catch (IOException e) {
            log.error("Failed to upload a file = {}", file);
            throw new BadRequestException(UPLOAD_FILE_EXCEPTION_MESSAGE.formatted(file.getName()));
        }
    }

    public FileDetails downloadFile(String fileId, FileType fileType) {
        val objectId = createObjectId(fileId, fileType);
        val s3Object = amazonS3.getObject(properties.getBucketName(), objectId);
        val fileDetails = new FileDetails();
        fileDetails.setFilename(fileId + getFileExtension(s3Object));
        fileDetails.setContentType(new ConfigurableMimeFileTypeMap().getContentType(fileDetails.getFilename()));

        try {
            fileDetails.setContent(s3Object.getObjectContent().readAllBytes());
        } catch (IOException e) {
            log.error("Failed to download a file by key = {}", objectId);
            throw new BadRequestException(DOWNLOAD_FILE_EXCEPTION_MESSAGE.formatted(objectId));
        }
        return fileDetails;
    }

    public void deleteFile(String fileId, FileType fileType) {
        val objectId = createObjectId(fileId, fileType);
        amazonS3.deleteObject(
                properties.getBucketName(),
                objectId
        );
    }

//    public Set<URL> getUrlsByObjectIds(List<String> fileIds, FileType fileType) {
//        checkListIfSizeGreaterThanMaxFilesCount(fileIds);
//        return fileIds
//                .stream()
//                .map(fileId -> getFileUrlById(fileId, fileType))
//                .collect(Collectors.toSet());
//    }

    private String createObjectId(String fileId, FileType fileType) {
        return fileType.getFolder() + fileId;
    }

    private String createFileName(String id) {
        return "id" + id;
    }

    private ObjectMetadata createMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(Map.of(EXTENSION, "." + getExtension(file.getOriginalFilename())));
        return metadata;
    }

    private FileMetaInfo createFileMetaInfo(String hash, String url, String filename) {
        return FileMetaInfo.builder()
                .hash(hash)
                .url(url)
                .fileName(filename)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private URL generateUrl(FileType fileType, String bucketName, String objectId) {
        val expirationTime = new DateTime().plusMinutes(properties.getExpirationTime()).toDate();
        if (FileType.CHECK == fileType) {
            return amazonS3.generatePresignedUrl(
                    bucketName,
                    objectId,
                    expirationTime
            );
        }
        return amazonS3.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, objectId));
    }

    private static String getHash(MultipartFile file) throws IOException {
        return String.valueOf(Hashing.murmur3_128().hashBytes(file.getBytes()).asLong());
    }

    private String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    private String getFileExtension(S3Object s3Object) {
        return s3Object
                .getObjectMetadata()
                .getUserMetadata()
                .get(EXTENSION);
    }
//    private void checkListIfSizeGreaterThanMaxFilesCount(List<String> fileIds) {
//        if (fileIds.size() > maxFilesCount) {
//            throw new IllegalArgumentException(
//                    NUMBER_OF_FILES_UPLOADED_EXCEEDED_EXCEPTION_MESSAGE + maxFilesCount
//            );
//        }
//    }
}