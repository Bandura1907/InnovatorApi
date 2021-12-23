package com.innovator.innovator.repository;

import com.innovator.innovator.models.RecommendationNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendatonNewsRepository extends JpaRepository<RecommendationNews, Integer> {
}
