package com.innovator.innovator.services;

import com.innovator.innovator.models.RecommendationNews;
import com.innovator.innovator.repository.RecommendatonNewsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecommendatonNewsService {

    private RecommendatonNewsRepository repository;

    public RecommendationNews saveNews(RecommendationNews recommendationNews) {
        return repository.save(recommendationNews);
    }
}
