package com.yandex.market.uploadservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yandex.market.exception.BadRequestException;
import com.yandex.market.uploadservice.config.properties.ObjectStorageProperties;
import com.yandex.market.uploadservice.model.FileMetaInfo;
import com.yandex.market.uploadservice.repository.FileMetaInfoRepository;
import com.yandex.market.uploadservice.validator.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.MurmurHash3;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.yandex.market.uploadservice.utils.Constants.UPLOAD_FILE_EXCEPTION_MESSAGE;
import static org.apache.commons.io.FilenameUtils.getExtension;

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

    public URL uploadFile(MultipartFile file) {
        validator.validate(file);
        try {
            InputStream mediaFileInputStream = file.getInputStream();
            long fileHash = getHash(file);
            Optional<FileMetaInfo> fileMetaInfoOptional = repository.findByHash(fileHash);

            if (fileMetaInfoOptional.isPresent()) {
                return new URL(fileMetaInfoOptional.get().getUrl());
            }

            String bucketName = properties.getBucketName();
            ObjectMetadata metadata = createMetadata(file);
            String objectId = generateId();
            String fileName = createFileName(objectId);
            String url = generateUrl(bucketName, objectId).toString();
            amazonS3.putObject(bucketName, objectId, mediaFileInputStream, metadata);
            FileMetaInfo fileMetaInfo = createFileMetaInfo(fileHash, url, fileName);
            fileMetaInfo = repository.save(fileMetaInfo);

            return new URL(fileMetaInfo.getUrl());
        } catch (IOException e) {
            log.error("Failed to upload a file = {}", file);
            throw new BadRequestException(UPLOAD_FILE_EXCEPTION_MESSAGE.formatted(file.getName()));
        }
    }

//    public FileDetails downloadFile(String fileId, FileType fileType) {
//        val objectId = createObjectId(fileId, fileType);
//        val s3Object = amazonS3.getObject(properties.getBucketName(), objectId);
//        val fileDetails = new FileDetails();
//        fileDetails.setFilename(fileId + getFileExtension(s3Object));
//        fileDetails.setContentType(new ConfigurableMimeFileTypeMap().getContentType(fileDetails.getFilename()));
//
//        try {
//            fileDetails.setContent(s3Object.getObjectContent().readAllBytes());
//        } catch (IOException e) {
//            log.error("Failed to download a file by key = {}", objectId);
//            throw new BadRequestException(DOWNLOAD_FILE_EXCEPTION_MESSAGE.formatted(objectId));
//        }
//        return fileDetails;
//    }

//    public void deleteFile(String fileId, FileType fileType) {
//        val objectId = createObjectId(fileId, fileType);
//        amazonS3.deleteObject(
//                properties.getBucketName(),
//                objectId
//        );
//    }

//    public Set<URL> getUrlsByObjectIds(List<String> fileIds, FileType fileType) {
//        checkListIfSizeGreaterThanMaxFilesCount(fileIds);
//        return fileIds
//                .stream()
//                .map(fileId -> getFileUrlById(fileId, fileType))
//                .collect(Collectors.toSet());
//    }

    private String generateId() {
        UUID uuid = UUID.randomUUID();
        String id = "%s-%s";
        String idFirstPart = uuid.toString().substring(0, 8);
        String idSecondPart = String.valueOf(Instant.now().getEpochSecond());
        return id.formatted(idFirstPart, idSecondPart);
    }

    private String createFileName(String id) {
        return "id" + id;
    }

    private ObjectMetadata createMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        //todo: проверить без 136 строчки
//        не понимаю зачем указывать расширение файла
        metadata.setUserMetadata(Map.of(EXTENSION, "." + getExtension(file.getOriginalFilename())));
        return metadata;
    }

    private FileMetaInfo createFileMetaInfo(long hash, String url, String filename) {
        return FileMetaInfo.builder()
                .hash(hash)
                .url(url)
                .fileName(filename)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private URL generateUrl(String bucketName, String objectId) {
        //todo: подумать над тем нужен ли закоменченный код
//        Date expirationTime = new DateTime().plusYears(properties.getUrlExpirationTimeInYears()).toDate();
//        if (FileType.CHECK == fileType) {
//            return amazonS3.generatePresignedUrl(
//                    bucketName,
//                    objectId,
//                    expirationTime
//            );
//        }
        return amazonS3.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, objectId));
    }

    private static long getHash(MultipartFile file) throws IOException {
        return MurmurHash3.hash32x86(file.getBytes());
    }

//    мб этот метод не нужен будет
//    private String getExtension(String filename) {
//        return FilenameUtils.getExtension(filename);
//    }

//    private String getFileExtension(S3Object s3Object) {
//        return s3Object
//                .getObjectMetadata()
//                .getUserMetadata()
//                .get(EXTENSION);
//    }
//    private void checkListIfSizeGreaterThanMaxFilesCount(List<String> fileIds) {
//        if (fileIds.size() > maxFilesCount) {
//            throw new IllegalArgumentException(
//                    NUMBER_OF_FILES_UPLOADED_EXCEEDED_EXCEPTION_MESSAGE + maxFilesCount
//            );
//        }
//    }
}