package com.yandex.market.uploadservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.yandex.market.uploadservice.config.properties.ObjectStorageProperties;
import com.yandex.market.uploadservice.model.FileMetaInfo;
import com.yandex.market.uploadservice.model.FileType;
import com.yandex.market.uploadservice.repository.FileMetaInfoRepository;
import com.yandex.market.uploadservice.validator.FileValidator;
import org.apache.commons.codec.digest.MurmurHash3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    private static final List<MultipartFile> MULTIPART_FILES = new ArrayList<>();
    private static final List<FileMetaInfo> FILE_META_INFOS = new ArrayList<>();

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private FileValidator validator;

    @Mock
    private FileMetaInfoRepository fileMetaInfoRepository;

    @Mock
    private ObjectStorageProperties properties;

    @InjectMocks
    private StorageService storageService;

    @BeforeAll
    public static void init() throws IOException {
        LocalDateTime timestamp = LocalDateTime.now();
        List<byte[]> filesBytes = readAllFiles();

        for (int i = 0; i < filesBytes.size(); i++) {
            MockMultipartFile multipartFile = new MockMultipartFile("file%s".formatted(i), filesBytes.get(i));
            MULTIPART_FILES.add(multipartFile);
        }

        for (int i = 0; i < MULTIPART_FILES.size(); i++) {
            int hash = getHash(MULTIPART_FILES.get(i));
            FILE_META_INFOS.add(createFileMetaInfo(String.valueOf(i), UUID.randomUUID(), hash, timestamp));
        }
    }

    @Test
    void getUrlsWhenAllOk() throws MalformedURLException {
        List<UUID> externalIds = FILE_META_INFOS.stream().map(FileMetaInfo::getExternalId).toList();
        String bucketName = "bucket";
        List<URL> expectedUrls = List.of(
                new URL("https://www.instagram.com/1"),
                new URL("https://www.instagram.com/2"),
                new URL("https://www.instagram.com/3")
        );

        mockUrlMethods(bucketName, externalIds, expectedUrls);

        List<URL> actualUrls = storageService.getUrls(externalIds);

        assertEquals(expectedUrls, actualUrls);

        verifyGetUrls(externalIds);
    }

    @Test
    void uploadFilesWhenAllOk() throws IOException {
        String bucketName = "bucket";
        UUID notExistInDbId = FILE_META_INFOS.get(0).getExternalId();
        List<UUID> actualExternalIds;

        List<UUID> expectedExternalIds = FILE_META_INFOS.stream()
                .map(FileMetaInfo::getExternalId)
                .toList();

        mockUploadMethods(bucketName, notExistInDbId);

        try (MockedStatic<UUID> mockedUuid = Mockito.mockStatic(UUID.class)) {
            mockedUuid.when(UUID::randomUUID).thenReturn(notExistInDbId);
            actualExternalIds = storageService.uploadFiles(MULTIPART_FILES, FileType.PRODUCT);
        }

        assertFalse(actualExternalIds.isEmpty());
        assertEquals(expectedExternalIds, actualExternalIds);
        verifyUpload(bucketName, notExistInDbId);
    }

    private void verifyGetUrls(List<UUID> externalIds) {
        verify(fileMetaInfoRepository).findByExternalIdIn(externalIds);

        verify(properties, times(3)).getBucketName();

        verify(amazonS3, times(3)).generatePresignedUrl(any(), any(), any());

        verifyNoMoreInteractions(amazonS3, validator, fileMetaInfoRepository, properties);
    }

    private void mockUrlMethods(String bucketName, List<UUID> externalIds, List<URL> expectedUrls) {
        Mockito.doReturn(FILE_META_INFOS)
                .when(fileMetaInfoRepository)
                .findByExternalIdIn(externalIds);

        Mockito.doReturn(bucketName)
                .when(properties)
                .getBucketName();

        Mockito.doReturn(expectedUrls.get(0))
                .when(amazonS3)
                .generatePresignedUrl(eq(bucketName), eq(externalIds.get(0).toString()), any());

        Mockito.doReturn(expectedUrls.get(1))
                .when(amazonS3)
                .generatePresignedUrl(eq(bucketName), eq(externalIds.get(1).toString()), any());

        Mockito.doReturn(expectedUrls.get(2))
                .when(amazonS3)
                .generatePresignedUrl(eq(bucketName), eq(externalIds.get(2).toString()), any());
    }

    private void verifyUpload(String bucketName, UUID externalId) {
        verify(validator).validate(eq(MULTIPART_FILES), eq(FileType.PRODUCT));

        verify(properties, times(3)).getBucketName();

        verify(amazonS3, times(1)).putObject(
                eq(bucketName),
                eq(externalId.toString()),
                any(),
                any(ObjectMetadata.class));

        verify(fileMetaInfoRepository, times(3)).findByHash(any(Long.class));

        verify(fileMetaInfoRepository, times(1)).saveAll(any(List.class));

        verifyNoMoreInteractions(amazonS3, validator, fileMetaInfoRepository, properties);
    }

    private void mockUploadMethods(String bucketName, UUID externalId) {
        Mockito.doNothing()
                .when(validator)
                .validate(eq(MULTIPART_FILES), eq(FileType.PRODUCT));

        Mockito.doReturn(bucketName)
                .when(properties)
                .getBucketName();

        Mockito.doReturn(Optional.empty())
                .when(fileMetaInfoRepository)
                .findByHash(FILE_META_INFOS.get(0).getHash());

        Mockito.doReturn(Optional.of(FILE_META_INFOS.get(1)))
                .when(fileMetaInfoRepository)
                .findByHash(FILE_META_INFOS.get(1).getHash());

        Mockito.doReturn(Optional.of(FILE_META_INFOS.get(2)))
                .when(fileMetaInfoRepository)
                .findByHash(FILE_META_INFOS.get(2).getHash());

        Mockito.doReturn(FILE_META_INFOS)
                .when(fileMetaInfoRepository)
                .saveAll(any());

        Mockito.doReturn(new PutObjectResult()).when(amazonS3).putObject(
                eq(bucketName),
                eq(externalId.toString()),
                any(),
                any(ObjectMetadata.class)
        );

    }

    private static FileMetaInfo createFileMetaInfo(String id, UUID uuid, int hash, LocalDateTime timestamp) {
        return FileMetaInfo.builder()
                .id(id)
                .externalId(uuid)
                .hash(hash)
                .timestamp(timestamp)
                .build();
    }

    private static List<byte[]> readAllFiles() throws IOException {

        byte[] file1Bytes = Files.readAllBytes(
                Paths.get("src", "test", "resources", "photos", "test_photo_0.jpeg")
        );

        byte[] file2Bytes = Files.readAllBytes(
                Paths.get("src", "test", "resources", "photos", "test_photo_1.jpg")
        );

        byte[] file3Bytes = Files.readAllBytes(
                Paths.get("src", "test", "resources", "photos", "test_photo_2.png")
        );

        return List.of(file1Bytes, file2Bytes, file3Bytes);
    }

    private static int getHash(MultipartFile file) throws IOException {
        return MurmurHash3.hash32x86(file.getBytes());
    }

}