package com.yandex.market.uploadservice.repository;

import com.yandex.market.uploadservice.model.FileMetaInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileMetaInfoRepository extends MongoRepository<FileMetaInfo, String> {
    Optional<FileMetaInfo> findByHash(String hash);
}