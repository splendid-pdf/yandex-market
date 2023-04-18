package com.yandex.market.uploadservice.validator;

import com.yandex.market.exception.BadRequestException;
import com.yandex.market.exception.SizeLimitFileExceededException;
import com.yandex.market.uploadservice.config.properties.FileRestrictionProperties;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static com.yandex.market.uploadservice.utils.Constants.*;


@Component
@RequiredArgsConstructor
public class FileValidator {

    private final FileRestrictionProperties properties;

    public void validate(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_FILE_EXCEPTION_MESSAGE);
        } else if (!ArrayUtils.contains(SUPPORTED_CONTENT_TYPES, file.getContentType())) {
            throw new BadRequestException(PERMITTED_FILE_TYPES_EXCEPTION_MESSAGE);
        }

        val maxFileSizeInBytes = getMaxFileSizeInBytes(file);

        if (file.getSize() > maxFileSizeInBytes) {
            throw new SizeLimitFileExceededException(
                    MAX_FILE_SIZE_EXCEPTION_MESSAGE,
                    file.getSize(),
                    maxFileSizeInBytes
            );
        }
    }

    private String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    private Long getMaxFileSizeInBytes(MultipartFile file) {
        return properties
                .getExtension()
                .get(getExtension(file.getOriginalFilename())).getMaxFileSizeInBytes();
    }
}