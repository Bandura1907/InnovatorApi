package com.innovator.innovator.repository;

import com.innovator.innovator.models.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Integer> {

    Optional<Articles> findArticlesByPictureName(String name);
}
