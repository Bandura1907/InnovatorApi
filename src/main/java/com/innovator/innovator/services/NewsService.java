package com.innovator.innovator.services;

import com.innovator.innovator.models.News;
import com.innovator.innovator.repository.NewsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@Slf4j
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

    public News findById(int id) {
        return newsRepository.findById(id).get();
    }

    public News saveNews(News news) {
        return newsRepository.save(news);
    }

    public void deleteNewsById(int id) {
        newsRepository.deleteById(id);
    }

    public void savePicture(MultipartFile file) throws FileUploadException {
        try {
            Path root = Paths.get(uploadPathPicture);
            Path resolve = root.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            if (resolve.toFile().exists()) {
                log.warn("File already exists: " + file.getOriginalFilename());
//                throw new FileUploadException("File already exists: " + file.getOriginalFilename());
            } else
                Files.copy(file.getInputStream(), resolve);

        } catch (Exception e) {
            throw new FileUploadException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void saveVideo(MultipartFile file) throws FileUploadException {
        try {
            String filename = file.getOriginalFilename();
            Path root = Paths.get(uploadPathVideo);
            Path resolve = root.resolve(filename);

            if (resolve.toFile().exists()) {
                log.warn("File already exists: " + file.getOriginalFilename());
//                throw new FileUploadException("File already exists: " + file.getOriginalFilename());
            } else
                Files.copy(file.getInputStream(), resolve);

        } catch (Exception e) {
            throw new FileUploadException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    protected Resource getResource(HttpServletRequest request) {
        final File file = (File) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(file);
    }
}
