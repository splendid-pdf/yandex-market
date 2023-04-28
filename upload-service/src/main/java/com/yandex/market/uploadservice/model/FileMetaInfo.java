package com.yandex.market.uploadservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "files_meta_info")
@EqualsAndHashCode(of = "externalId")
public class FileMetaInfo {
    @Id
    private String id;
    private UUID externalId;
    private int hash;
    private LocalDateTime timestamp;
}