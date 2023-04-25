package com.yandex.market.uploadservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.yandex.market.exception.BadRequestException;
import com.yandex.market.exception.NotFoundException;
import com.yandex.market.uploadservice.config.properties.ObjectStorageProperties;
import com.yandex.market.uploadservice.model.FileMetaInfo;
import com.yandex.market.uploadservice.model.FileType;
import com.yandex.market.uploadservice.repository.FileMetaInfoRepository;
import com.yandex.market.uploadservice.validator.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.MurmurHash3;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.yandex.market.uploadservice.utils.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final AmazonS3 amazonS3;
    private final FileValidator validator;
    private final FileMetaInfoRepository fileMetaInfoRepository;
    private final ObjectStorageProperties properties;

    public List<UUID> uploadFiles(List<MultipartFile> files, FileType fileType) {
        validator.validate(files, fileType);

        List<FileMetaInfo> fileMetaInfos = files.stream()
                .map(file -> uploadFile(file, fileType))
                .toList();

        fileMetaInfos = fileMetaInfoRepository.saveAll(fileMetaInfos);

        return fileMetaInfos.stream()
                .map(FileMetaInfo::getExternalId)
                .toList();
    }

    public List<URL> getUrls(List<UUID> filesIds) {
        List<FileMetaInfo> fileMetaInfos = fileMetaInfoRepository.findByExternalIdIn(filesIds);

        if(fileMetaInfos.isEmpty())  {
            throw new NotFoundException(ENTITIES_WAS_NOT_FOUND_BY_IDS_LIST_EXCEPTION_MESSAGE);
        }

        return fileMetaInfos.stream()
                .map(f -> generateUrl(properties.getBucketName(), f.getExternalId().toString()))
                .toList();
    }

    private FileMetaInfo uploadFile(MultipartFile file, FileType fileType) {
        try {
            String bucketName = properties.getBucketName();
            InputStream mediaFileInputStream = file.getInputStream();
            int fileHash = getHash(file);
            Optional<FileMetaInfo> fileMetaInfoOptional = fileMetaInfoRepository.findByHash(fileHash);

            if (fileMetaInfoOptional.isPresent()) {
                return fileMetaInfoOptional.get();
            }

            ObjectMetadata metadata = createMetadata(file);
            UUID externalId = UUID.randomUUID();
            amazonS3.putObject(bucketName, externalId.toString(), mediaFileInputStream, metadata);

            return createFileMetaInfo(externalId, fileHash);
        } catch (IOException e) {
            log.error("Failed to upload a file = {} fileType ={}", file, fileType);
            throw new BadRequestException(UPLOAD_FILE_EXCEPTION_MESSAGE.formatted(file.getName()));
        }
    }

    private ObjectMetadata createMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }

    private FileMetaInfo createFileMetaInfo(UUID externalId, int hash) {
        return FileMetaInfo.builder()
                .hash(hash)
                .externalId(externalId)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private URL generateUrl(String bucketName, String externalId) {
        return amazonS3.generatePresignedUrl(bucketName, externalId, new DateTime().plusMinutes(15).toDate());
    }

    private int getHash(MultipartFile file) throws IOException {
        return MurmurHash3.hash32x86(file.getBytes());
    }
}