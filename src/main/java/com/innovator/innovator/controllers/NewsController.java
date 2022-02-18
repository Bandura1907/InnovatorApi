package com.innovator.innovator.controllers;

import com.innovator.innovator.MultipartUploadFile;
import com.innovator.innovator.models.News;
import com.innovator.innovator.models.UserAuth;
import com.innovator.innovator.models.VideoMetadata;
import com.innovator.innovator.security.services.UserDetailsServiceImpl;
import com.innovator.innovator.services.NewsService;
import com.innovator.innovator.services.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class NewsController {

    @Value("${upload.path.photo}")
    private String uploadPathPicture;

    @Value("${upload.path.video}")
    private String uploadPathVideo;

    private final NewsService newsService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public NewsController(NewsService newsService, UserDetailsServiceImpl userDetailsService) {
        this.newsService = newsService;
        this.userDetailsService = userDetailsService;
    }


    @GetMapping("/news")
    public ResponseEntity<List<News>> getNews(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(newsService.findAllByPaging(PageRequest.of(page, 30)).getContent());
    }

    @GetMapping("/news_for_front")
    public ResponseEntity<Map<String, Object>> getFront(@RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 27);
        Page<News> pageTuts = newsService.findAllByPaging(pageable);

        List<News> news = pageTuts.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("news", news);
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/news_id/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable int id) {
        return ResponseEntity.ok(newsService.findById(id).get());
    }

    @GetMapping("/news/photo/{name}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String name) throws IOException {
        try {
            MultipartUploadFile image = new MultipartUploadFile(uploadPathPicture + name);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image.getPhotoFile().get());
        } catch (Exception ex) {
            log.error("error reading file: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping(value = "/news/photo/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public ResponseEntity<StreamingResponseBody> getPicture(@PathVariable String name) throws IOException {
//        InputStream inputStream = Files.newInputStream(Path.of(uploadPathPicture, name));
//        return ResponseEntity.ok(inputStream::transferTo);
//    }


    @GetMapping(value = "/video/{name}")
    public void getVideo(@PathVariable String name, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File videoFile = new File(uploadPathVideo + name);
        request.setAttribute(NewsService.ATTR_FILE, videoFile);
        newsService.handleRequest(request, response);
    }


    @PostMapping("/news_add")
    public ResponseEntity<News> addNews(@RequestBody News news) {
        UserAuth userAuth = userDetailsService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        news.setUser(userAuth);
        return new ResponseEntity<>(newsService.saveNews(news), HttpStatus.CREATED);
    }

    @PostMapping(value = "/save_picture", consumes = {"multipart/form-data"})
    public ResponseEntity<Void> savePicture(@RequestParam("picture") MultipartFile picture) throws FileUploadException {
        newsService.savePicture(picture);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/save_video", consumes = {"multipart/form-data"})
    public ResponseEntity<Void> saveVideo(@RequestParam("video") MultipartFile video) throws FileUploadException {
        newsService.saveVideo(video);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/news_edit/{id}")
    public ResponseEntity<News> editNews(@PathVariable int id, @RequestBody News newsBody) {
        News news = newsService.findById(id).get();
        news.setPictureUrl(newsBody.getPictureUrl());
        news.setSourceUrl(newsBody.getSourceUrl());
        news.setText(newsBody.getText());
        news.setVideoUrl(newsBody.getVideoUrl());
        news.setTitle(newsBody.getTitle());
        news.setSubtitle(newsBody.getSubtitle());
        news.setVideoName(newsBody.getVideoName());

        return ResponseEntity.ok(newsService.saveNews(news));
    }

    @DeleteMapping("/delete_news/{id}")
    public ResponseEntity<Map<String, Object>> deleteNews(@PathVariable int id, @RequestParam(defaultValue = "0") int page) {
        newsService.deleteNewsById(id);
        Page<News> newsPage = newsService.findAllByPaging(PageRequest.of(page, 27));
        return ResponseEntity.ok(Map.of("totalPages", newsPage.getTotalPages()));
    }

    @ExceptionHandler
    public ResponseEntity<Void> notFoundExceptionHandler(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
