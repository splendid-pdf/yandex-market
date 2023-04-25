package com.yandex.market.uploadservice.repository;

import com.yandex.market.uploadservice.model.FileMetaInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileMetaInfoRepository extends MongoRepository<FileMetaInfo, String> {

    Optional<FileMetaInfo> findByHash(long hash);

    List<FileMetaInfo> findByExternalIdIn(List<UUID> externalId);

}