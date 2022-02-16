package com.innovator.innovator.services;

import com.innovator.innovator.MultipartUploadFile;
import com.innovator.innovator.models.News;
import com.innovator.innovator.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class NewsService extends ResourceHttpRequestHandler {

    public static final String ATTR_FILE = NewsService.class.getName() + ".file";

    @Value("${upload.path.photo}")
    private String uploadPathPicture;

    @Value("${upload.path.video}")
    private String uploadPathVideo;

    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public Page<News> findAllByPaging(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    public Optional<News> findById(int id) {
        return newsRepository.findById(id);
    }

    public News saveNews(News news) {
        return newsRepository.save(news);
    }

    public void deleteNewsById(int id) {
        newsRepository.deleteById(id);
    }

    public void savePicture(MultipartFile file) throws FileUploadException {
        new MultipartUploadFile(uploadPathPicture).saveFile(file);

    }

    public void saveVideo(MultipartFile file) throws FileUploadException {
        new MultipartUploadFile(uploadPathVideo).saveFile(file);
    }

    public Optional<StreamBytesInfo> getStreamBytesInfo(Integer id, HttpRange range) {
        Optional<News> byId = newsRepository.findById(id);
        if (byId.isEmpty())
            return Optional.empty();

        Path filePath = Path.of(uploadPathVideo, byId.get().getVideoName());
        if (!Files.exists(filePath))
            return Optional.empty();

        try {
            long fileSize = Files.size(filePath);
            long chunkSize = fileSize / 100;
            if (range == null) {
                return Optional.of(new StreamBytesInfo(
                        out -> Files.newInputStream(filePath).transferTo(out), fileSize, 0, fileSize, "video/mp4"
                ));
            }

            long rangeStart = range.getRangeStart(0);
            long rangeEnd = rangeStart + chunkSize;
            if (rangeEnd >= fileSize)
                rangeEnd = fileSize - 1;

            long finalRangeEnd = rangeEnd;
            return Optional.of(new StreamBytesInfo(
                    out -> {
                        try(InputStream inputStream = Files.newInputStream(filePath)) {
                            inputStream.skip(rangeStart);
                            byte[] bytes = inputStream.readNBytes((int) ((finalRangeEnd - rangeStart) + 1));
                            out.write(bytes);
                        }
                    }, fileSize, rangeStart, rangeEnd, "video/mp4"
            ));

        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    protected Resource getResource(HttpServletRequest request) {
        final File file = (File) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(file);
    }
}
