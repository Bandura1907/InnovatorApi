package com.innovator.innovator.services;

import com.innovator.innovator.models.News;
import com.innovator.innovator.repository.NewsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsService {

    private NewsRepository newsRepository;

    public Page<News> findAllByPaging(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    public News saveNews(News news) {
        return newsRepository.save(news);
    }

    public void deleteNewsById(int id) {
        newsRepository.deleteById(id);
    }
}
