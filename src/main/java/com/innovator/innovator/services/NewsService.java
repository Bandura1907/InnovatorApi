package com.innovator.innovator.services;

import com.innovator.innovator.MultipartUploadFile;
import com.innovator.innovator.models.News;
import com.innovator.innovator.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
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

    @Cacheable("newsCache")
    public Page<News> findAllByPaging(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    @Cacheable(value = "newsCache", key = "#id")
    public Optional<News> findById(int id) {
        return newsRepository.findById(id);
    }

    @CachePut(value = "newsCache", key = "#news.id")
    public News saveNews(News news) {
        return newsRepository.save(news);
    }

    @CacheEvict(value = "newsCache", key = "#id")
    public void deleteNewsById(int id) {
        newsRepository.deleteById(id);
    }

    public void savePicture(MultipartFile file) throws FileUploadException {
        new MultipartUploadFile(uploadPathPicture).saveFile(file);

    }

    public void saveVideo(MultipartFile file) throws FileUploadException {
        new MultipartUploadFile(uploadPathVideo).saveFile(file);
    }
    

    @Override
    protected Resource getResource(HttpServletRequest request) {
        final File file = (File) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(file);
    }
}
