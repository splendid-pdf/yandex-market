package com.yandex.market.uploadservice.validator;

import com.yandex.market.exception.ValidationException;
import com.yandex.market.uploadservice.config.properties.FileRestriction;
import com.yandex.market.uploadservice.config.properties.ValidationProperties;
import com.yandex.market.uploadservice.model.FileType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.yandex.market.uploadservice.utils.Constants.*;


@Component
@RequiredArgsConstructor
public class FileValidator {

    private final ValidationProperties properties;

    @SneakyThrows
    public void validate(List<MultipartFile> files, FileType fileType) {
        List<String> exceptionMessages = new ArrayList<>();
        files.forEach(file -> validateFile(file, fileType, exceptionMessages));

        if (!CollectionUtils.isEmpty(exceptionMessages)) {
            throw new ValidationException(String.join(" | ", exceptionMessages));
        }

    }

    private void validateFile(MultipartFile file, FileType fileType, List<String> exceptionMessages) {
        String fileName = file.getOriginalFilename();

        if (file.isEmpty()) {
            exceptionMessages.add(EMPTY_FILE_EXCEPTION_MESSAGE);
            return;
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!properties.getExtensions().containsKey(extension)) {
            exceptionMessages.add(NOT_PERMITTED_FILE_EXTENSION_EXCEPTION_MESSAGE.formatted(fileName));
            return;
        }

        FileRestriction fileRestriction = properties.getExtensions().get(extension);

        if (!fileRestriction.getFileTypes().contains(fileType)) {
            exceptionMessages.add(NOT_PERMITTED_FILE_TYPE_BY_FILE_EXTENSION_EXCEPTION_MESSAGE
                    .formatted(fileName, fileType, extension)
            );
            return;
        }

        long fileSizeInBytes = file.getSize();

        if (fileRestriction.getMaxFileSizeInBytes() < fileSizeInBytes) {
            exceptionMessages.add(MAX_FILE_SIZE_EXCEPTION_MESSAGE.formatted(fileName));
        }
    }

}