package com.innovator.innovator.services;

import com.innovator.innovator.payload.request.NewVideoRequest;
import com.innovator.innovator.models.VideoMetadata;
import com.innovator.innovator.repository.VideoMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
@Transactional
@Slf4j
public class VideoStreamService extends ResourceHttpRequestHandler {

    public static final String ATTR_FILE = NewsService.class.getName() + ".file";

    @Value("${upload.path.video}")
    private String dataFolder;
    private final VideoMetadataRepository videoMetadataRepository;

    @Autowired
    public VideoStreamService(VideoMetadataRepository videoMetadataRepository) {
        this.videoMetadataRepository = videoMetadataRepository;
    }

    public VideoMetadata saveNewVideo(NewVideoRequest newVideoRequest) {
        VideoMetadata metadata = new VideoMetadata();
        metadata.setFileName(newVideoRequest.getFile().getOriginalFilename());
        metadata.setContentType(newVideoRequest.getFile().getContentType());
        metadata.setFileSize(newVideoRequest.getFile().getSize());
        VideoMetadata videoMetadataSaved = videoMetadataRepository.save(metadata);

        Path directory = Path.of(dataFolder);
        try {
//            Files.createDirectory(directory);
            Path file = Path.of(directory.toString(), newVideoRequest.getFile().getOriginalFilename());
            if (!file.toFile().exists())
                try (OutputStream outputStream = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                    newVideoRequest.getFile().getInputStream().transferTo(outputStream);
                }
        } catch (IOException ex) {
            log.error("", ex);
            throw new IllegalStateException(ex);
        }

        return videoMetadataSaved;
    }

    @Override
    protected Resource getResource(HttpServletRequest request) {
        final File file = (File) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(file);
    }
}
