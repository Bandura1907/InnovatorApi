package com.innovator.innovator.services;

import com.innovator.innovator.models.Recommendation;
import com.innovator.innovator.repository.RecommendationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecommendationService {

    private RecommendationRepository recommendationRepository;

    public void saveRecommendation(Recommendation recommendation) {
        recommendationRepository.save(recommendation);
    }
}
