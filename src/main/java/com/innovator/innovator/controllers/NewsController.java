package com.innovator.innovator.controllers;

import com.innovator.innovator.models.News;
import com.innovator.innovator.services.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api")
public class NewsController {

    private NewsService newsService;

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

    @PostMapping("/news_add")
    public ResponseEntity<News> addNews(@RequestBody News news) {
        return new ResponseEntity<>(newsService.saveNews(news), HttpStatus.CREATED);
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
