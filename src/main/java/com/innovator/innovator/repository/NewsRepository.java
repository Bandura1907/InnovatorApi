package com.innovator.innovator.repository;

import com.innovator.innovator.models.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer>, PagingAndSortingRepository<News, Integer> {

    Page<News> findAll(Pageable pageable);
}
