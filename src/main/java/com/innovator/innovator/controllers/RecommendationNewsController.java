package com.innovator.innovator.controllers;

import com.innovator.innovator.models.RecommendationNews;
import com.innovator.innovator.models.User;
import com.innovator.innovator.services.RecommendationNewsService;
import com.innovator.innovator.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RecommendationNewsController {

    private UserService userService;
    private RecommendationNewsService recommendatonNewsService;

    @PostMapping("/send_recommendation_news/{clientId}")
    public ResponseEntity<Map<String, String>> sendRecNews(@PathVariable int clientId, @RequestBody RecommendationNews recommendationNews) {
        User user = userService.findById(clientId);
        recommendationNews.setUser(user);
        recommendatonNewsService.saveNews(recommendationNews);

        return ResponseEntity.ok(Map.of("message", "Message sender"));
    }
}
