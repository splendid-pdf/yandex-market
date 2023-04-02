package com.yandex.market.uploadservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "file_meta_info")
public class FileMetaInfo {
    @Id
    private String id;
    private String hash;
    private String fileName;
    private String idempotencyKey;
    private OffsetDateTime timestamp;
}