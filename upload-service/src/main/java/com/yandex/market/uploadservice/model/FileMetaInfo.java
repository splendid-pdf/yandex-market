package com.yandex.market.uploadservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "file_meta_info")
public class FileMetaInfo {
    @Id
    private String id;
    private String url;
    private String hash;
    private String fileName;
    private LocalDateTime timestamp;
}