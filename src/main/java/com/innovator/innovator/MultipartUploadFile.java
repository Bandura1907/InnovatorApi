package com.innovator.innovator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class MultipartUploadFile {

    private String uploadPath;

    public void saveFile(MultipartFile multipartFile) throws FileUploadException {
        try {
            Path root = Paths.get(uploadPath);
            Path resolve = root.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));

            if (!root.toFile().exists())
                root.toFile().mkdirs();

            if (resolve.toFile().exists()) {
                log.warn("File already exists: " + multipartFile.getOriginalFilename());
            } else
                Files.copy(multipartFile.getInputStream(), resolve);

        } catch (Exception e) {
            throw new FileUploadException("Could not store the file. Error: " + e.getMessage());
        }
    }


    public byte[] getPhotoFile() {
        byte[] image = new byte[0];

        try {
            image = FileUtils.readFileToByteArray(new File(uploadPath));
        } catch (IOException e) {
            log.error(uploadPath + " (Не удается найти указанный файл)");
        }

        return image;
    }
}
