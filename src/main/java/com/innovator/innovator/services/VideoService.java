package com.innovator.innovator.services;

import com.innovator.innovator.controllers.repr.NewVideoRepr;
import com.innovator.innovator.controllers.repr.VideoMetadataRepr;
import com.innovator.innovator.models.VideoMetadata;
import com.innovator.innovator.repository.VideoMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoService {

    @Value("${upload.path.video}")
    private String dataFolder;

    private final VideoMetadataRepository videoMetadataRepository;

    @Autowired
    public VideoService(VideoMetadataRepository videoMetadataRepository) {
        this.videoMetadataRepository = videoMetadataRepository;
    }

    public List<VideoMetadataRepr> findAllVideoMetadata() {
        return videoMetadataRepository.findAll().stream()
                .map(VideoService::convert)
                .collect(Collectors.toList());
    }

    public Optional<VideoMetadataRepr> findById(Integer id) {
        return videoMetadataRepository.findById(id).map(VideoService::convert);
    }

    private static VideoMetadataRepr convert(VideoMetadata vmd) {
        VideoMetadataRepr repr = new VideoMetadataRepr();
        repr.setId(vmd.getId());
        repr.setStreamUrl("/api/video/stream/" + vmd.getId());
        repr.setDescription(vmd.getDescription());
        repr.setContentType(vmd.getContentType());
        return repr;
    }

    @Transactional
    public VideoMetadata saveNewVideo(NewVideoRepr newVideoRepr) {
        VideoMetadata metadata = new VideoMetadata();
        metadata.setFileName(newVideoRepr.getFile().getOriginalFilename());
        metadata.setContentType(newVideoRepr.getFile().getContentType());
        metadata.setFileSize(newVideoRepr.getFile().getSize());
        VideoMetadata videoMetadataSaved = videoMetadataRepository.save(metadata);

        Path directory = Path.of(dataFolder);
        try {
//            Files.createDirectory(directory);
            Path file = Path.of(directory.toString(), newVideoRepr.getFile().getOriginalFilename());
            if (!file.toFile().exists())
                try (OutputStream outputStream = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                    newVideoRepr.getFile().getInputStream().transferTo(outputStream);
                }
        } catch (IOException ex) {
            log.error("", ex);
            throw new IllegalStateException(ex);
        }

        return videoMetadataSaved;
    }

    public Optional<StreamBytesInfo> getStreamBytesInfo(Integer id, HttpRange range) {
        Optional<VideoMetadata> byId = videoMetadataRepository.findById(id);
        if (byId.isEmpty()) {
            return Optional.empty();
        }
        Path filePath = Path.of(dataFolder, byId.get().getFileName());
        if (!Files.exists(filePath)) {
            log.error("File {} not found", filePath);
            return Optional.empty();
        }
        try {
            long fileSize = Files.size(filePath);
            long chunkSize = fileSize / 100;
            if (range == null) {
                return Optional.of(new StreamBytesInfo(
                        out -> Files.newInputStream(filePath).transferTo(out),
                        fileSize, 0, fileSize, byId.get().getContentType()));
            }

            long rangeStart = range.getRangeStart(0);
            long rangeEnd = rangeStart + chunkSize; // range.getRangeEnd(fileSize);
            if (rangeEnd >= fileSize) {
                rangeEnd = fileSize - 1;
            }
            long finalRangeEnd = rangeEnd;
            return Optional.of(new StreamBytesInfo(
                    out -> {
                        try (InputStream inputStream = Files.newInputStream(filePath)) {
                            inputStream.skip(rangeStart);
                            byte[] bytes = inputStream.readNBytes((int) ((finalRangeEnd - rangeStart) + 1));
                            out.write(bytes);
                        }
                    },
                    fileSize, rangeStart, rangeEnd, byId.get().getContentType()));
        } catch (IOException ex) {
            log.error("", ex);
            return Optional.empty();
        }
    }
}
