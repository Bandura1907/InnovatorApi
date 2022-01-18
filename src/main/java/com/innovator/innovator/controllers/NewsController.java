package com.innovator.innovator.controllers;

import com.innovator.innovator.models.News;
import com.innovator.innovator.services.NewsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
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

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
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
        return ResponseEntity.ok(newsService.findById(id));
    }

    @GetMapping("/news/photo/{name}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String name) throws IOException {
        try {
            File image = new File(uploadPathPicture + name);
            byte[] imageBytes = Files.readAllBytes(image.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageBytes.length);

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (NoSuchFileException ex) {
            log.error("error reading file: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/video/{name}")
    public void getVideo(@PathVariable String name, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute(NewsService.ATTR_FILE,
                new File(uploadPathVideo + name));
        newsService.handleRequest(request, response);

//        InputStream is = new FileInputStream(uploadPathVideo + name);
//        byte[] videoResource = IOUtils.toByteArray(is);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType("video/mp4"))
//                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=video_%s.%s", 1, "mp4"))
//                .body(videoResource);
    }


    @PostMapping("/news_add")
    public ResponseEntity<News> addNews(@RequestBody News news) {
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
        News news = newsService.findById(id);
        news.setPictureUrl(newsBody.getPictureUrl());
        news.setSourceUrl(newsBody.getSourceUrl());
        news.setText(newsBody.getText());
        news.setVideoUrl(newsBody.getVideoUrl());

        return ResponseEntity.ok(newsService.saveNews(news));
    }

    @DeleteMapping("/delete_news/{id}")
    public ResponseEntity<Map<String, Object>> deleteNews(@PathVariable int id, @RequestParam(defaultValue = "0") int page) {
        newsService.deleteNewsById(id);

        Page<News> newsPage = newsService.findAllByPaging(PageRequest.of(page, 27));

        return ResponseEntity.ok(Map.of(
                "news", newsPage.getContent(),
                "currentPage", newsPage.getNumber(),
                "totalItems", newsPage.getTotalElements(),
                "totalPages", newsPage.getTotalPages()
        ));
    }
}
