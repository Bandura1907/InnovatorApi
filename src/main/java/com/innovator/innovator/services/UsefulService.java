package com.innovator.innovator.services;

import com.innovator.innovator.models.Articles;
import com.innovator.innovator.models.Videos;
import com.innovator.innovator.repository.ArticlesRepository;
import com.innovator.innovator.repository.VideosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsefulService {

    private ArticlesRepository articlesRepository;
    private VideosRepository videosRepository;

    public List<Articles> findAllArticles() {
        return articlesRepository.findAll();
    }

    public List<Videos> findAllVideos() {
        return videosRepository.findAll();
    }

    public Optional<Articles> findArticlesByPictureName(String name) {
        return articlesRepository.findArticlesByPictureName(name);
    }

    public Optional<Videos> findVideosByPictureName(String name) {
        return videosRepository.findVideosByPictureName(name);
    }

    public Optional<Articles> findArticleById(int id) {
        return articlesRepository.findById(id);
    }


    public Optional<Videos> findVideoById(int id) {
        return videosRepository.findById(id);
    }

    public Articles saveArticle(Articles articles) {
       return articlesRepository.save(articles);
    }

    public Videos saveVideo(Videos videos) {
        return videosRepository.save(videos);
    }

    public void deleteArticleById(int id) {
        articlesRepository.deleteById(id);
    }

    public void deleteVideoById(int id) {
        videosRepository.deleteById(id);
    }

//    private UsefulRepository usefulRepository;
//
//    public List<Useful> findAll() {
//        return usefulRepository.findAll();
//    }
//
//    public Optional<Useful> findById(int id) {
//        return usefulRepository.findById(id);
//    }
}
