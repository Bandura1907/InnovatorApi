package com.innovator.innovator.repository;

import com.innovator.innovator.models.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideosRepository extends JpaRepository<Videos, Integer> {
    Optional<Videos> findVideosByPictureName(String name);
}
