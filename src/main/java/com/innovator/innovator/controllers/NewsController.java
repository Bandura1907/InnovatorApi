package com.innovator.innovator.controllers;

import com.innovator.innovator.models.News;
import com.innovator.innovator.services.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/news_add")
    public ResponseEntity<News> addNews(@RequestBody News news) {
        return new ResponseEntity<>(newsService.saveNews(news), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete_news/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable int id) {
        newsService.deleteNewsById(id);

        return ResponseEntity.ok().build();
    }
}
