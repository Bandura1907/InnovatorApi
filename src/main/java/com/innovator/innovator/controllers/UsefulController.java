package com.innovator.innovator.controllers;

import com.innovator.innovator.models.Articles;
import com.innovator.innovator.models.Videos;
import com.innovator.innovator.payload.response.MessageResponse;
import com.innovator.innovator.services.UsefulService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UsefulController {

    private UsefulService usefulService;

    @GetMapping("/get_useful")
    public ResponseEntity<?> getUseful() {
        List<Articles> articles = usefulService.findAllArticles();
        List<Videos> videos = usefulService.findAllVideos();
        return ResponseEntity.ok(Map.of("articles", articles,
                "videos", videos));
    }

    @GetMapping("/useful/get_article/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable int id) {
        Optional<Articles> articles = usefulService.findArticleById(id);
        if (articles.isEmpty())
            return new ResponseEntity<>(new MessageResponse("Article not found"), HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(articles.get());
    }

    @GetMapping("/useful/get_video/{id}")
    public ResponseEntity<?> getVideoById(@PathVariable int id) {
        Optional<Videos> videos = usefulService.findVideoById(id);
        if (videos.isEmpty())
            return new ResponseEntity<>(new MessageResponse("Article not found"), HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(videos.get());
    }

    @GetMapping("/useful/get_picture/{name}/{index}")
    @Cacheable("images")
    public ResponseEntity<?> getPicture(@PathVariable String name, @PathVariable int index) {
        byte[] image = new byte[0];
        switch (index) {
            case 0:
                Optional<Articles> articles = usefulService.findArticlesByPictureName(name);
                if (articles.isEmpty())
                    return new ResponseEntity<>(new MessageResponse("Article not found"), HttpStatus.NOT_FOUND);

                image = articles.get().getPicture();
                break;
            case 1:
                Optional<Videos> videos = usefulService.findVideosByPictureName(name);
                if (videos.isEmpty())
                    return new ResponseEntity<>(new MessageResponse("Video not found"), HttpStatus.NOT_FOUND);

                image = videos.get().getPicture();
                break;

        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @PostMapping("/useful/add_video")
    public ResponseEntity<?> addVideo(@RequestParam("name") String name,
                                      @RequestParam("pictureUrl") String pictureUrl,
                                      @RequestParam("videoUrl") String videoUrl,
                                      @RequestParam("picture") MultipartFile picture) throws IOException {
        String pictureName = UUID.randomUUID() + StringUtils.cleanPath(picture.getOriginalFilename());

        Videos videos = new Videos();
        videos.setName(name);
        videos.setPicture(picture.getBytes());
        videos.setPictureName(pictureName);
        videos.setPictureUrl(pictureUrl + pictureName + "/1");
        videos.setVideoUrl(videoUrl);

        return ResponseEntity.ok(usefulService.saveVideo(videos));
    }

    @PostMapping("/useful/add_article")
    public ResponseEntity<?> addArticle(@RequestParam("name") String name,
                                        @RequestParam("description") String description,
                                        @RequestParam("pictureUrl") String pictureUrl,
                                        @RequestParam("picture") MultipartFile picture) throws IOException {
        String pictureName = UUID.randomUUID() + StringUtils.cleanPath(picture.getOriginalFilename());

        Articles articles = new Articles();
        articles.setDescription(description);
        articles.setName(name);
        articles.setPictureUrl(pictureUrl + pictureName + "/0");
        articles.setPicture(picture.getBytes());
        articles.setPictureName(pictureName);

        return ResponseEntity.ok(usefulService.saveArticle(articles));
    }

    @PutMapping("/useful/edit_article/{id}")
    public ResponseEntity<?> editArticle(@PathVariable int id, @RequestParam("name") String name,
                                         @RequestParam("description") String description,
                                         @RequestParam("pictureUrl") String pictureUrl,
                                         @RequestParam(value = "picture", required = false) MultipartFile picture) throws IOException {
        Optional<Articles> articles = usefulService.findArticleById(id);
        if (articles.isEmpty())
            return new ResponseEntity<>(new MessageResponse("Article not found"), HttpStatus.NOT_FOUND);

        articles.get().setName(name);
        articles.get().setDescription(description);

        if (picture != null) {
            String pictureName = UUID.randomUUID() + StringUtils.cleanPath(picture.getOriginalFilename());
            articles.get().setPicture(picture.getBytes());
            articles.get().setPictureUrl(pictureUrl != null ? pictureUrl + pictureName + "/0" : articles.get().getPictureUrl());
            articles.get().setPictureName(pictureUrl != null ? pictureName : articles.get().getPictureName());
        }


        return ResponseEntity.ok(usefulService.saveArticle(articles.get()));
    }

    @PutMapping("/useful/edit_video/{id}")
    public ResponseEntity<?> editVideo(@PathVariable int id, @RequestParam("name") String name,
                                       @RequestParam("pictureUrl") String pictureUrl,
                                       @RequestParam("videoUrl") String videoUrl,
                                       @RequestParam(value = "picture", required = false) MultipartFile picture) throws IOException {
        Optional<Videos> videos = usefulService.findVideoById(id);
        if (videos.isEmpty())
            return new ResponseEntity<>(new MessageResponse("Video not found"), HttpStatus.NOT_FOUND);

        videos.get().setName(name);
        videos.get().setVideoUrl(videoUrl != null ? videoUrl : videos.get().getVideoUrl());

        if (picture !=null) {
            String pictureName = UUID.randomUUID() + StringUtils.cleanPath(picture.getOriginalFilename());
            videos.get().setPicture(picture.getBytes());
            videos.get().setPictureUrl(pictureUrl != null ? pictureUrl + pictureName + "/1" : videos.get().getPictureUrl());
            videos.get().setPictureName(pictureUrl != null ? pictureName : videos.get().getPictureName());
        }

        return ResponseEntity.ok(usefulService.saveVideo(videos.get()));
    }

    @DeleteMapping("/useful/delete_article/{id}")
    public void deleteArticleById(@PathVariable int id) {
        usefulService.deleteArticleById(id);
    }

    @DeleteMapping("/useful/delete_video/{id}")
    public void deleteVideoById(@PathVariable int id) {
        usefulService.deleteVideoById(id);
    }

//    @GetMapping("/get_useful")
//    public ResponseEntity<List<Useful>> getUseful() {
//        return ResponseEntity.ok(usefulService.findAll());
//    }
//
//    @GetMapping("/get_useful/{id}")
//    public ResponseEntity<?> getUsefulId(@PathVariable int id) {
//        Optional<Useful> useful = usefulService.findById(id);
//
//        if (useful.isEmpty())
//            return new ResponseEntity<>(new MessageResponse("Useful not found"), HttpStatus.NOT_FOUND);
//
//        return ResponseEntity.ok(useful.get());
//    }

//    private UsefulRepository usefulRepository;
//    private UserAuthRepository userAuthRepository;
//
//    @GetMapping("/useful")
//    public ResponseEntity<List<Useful>> getUsefulAll() {
//        return  ResponseEntity.ok(usefulRepository.findAll());
//    }
//
//    @GetMapping("/useful/{id}")
//    public ResponseEntity<Useful> getUseful(@PathVariable Integer id) {
//        Optional<Useful> useful = usefulRepository.findById(id);
//
//        if (useful.isEmpty())
//            return ResponseEntity.notFound().build();
//
//        return ResponseEntity.ok(useful.get());
//    }
//
//    @PostMapping("/useful")
//    public ResponseEntity<Useful> addUseful(@RequestBody Useful useful) {
//        UserAuth userAuth = userAuthRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
//        useful.setUser(userAuth);
//        return ResponseEntity.ok(usefulRepository.save(useful));
//    }
//
//    @PutMapping("/useful/{id}")
//    public ResponseEntity<Useful> editUseful(@PathVariable Integer id, @RequestBody Useful useful) {
//        Optional<Useful> usefulOptional = usefulRepository.findById(id);
//
//        if (usefulOptional.isEmpty())
//            return ResponseEntity.notFound().build();
//
//        usefulOptional.get().setTitle(useful.getTitle());
//        usefulOptional.get().setDescription(useful.getDescription());
//        usefulOptional.get().setCategory(useful.getCategory());
//        usefulOptional.get().setImageUrl(useful.getImageUrl());
//
//
//        return ResponseEntity.ok(usefulRepository.save(usefulOptional.get()));
//    }
//
//    @DeleteMapping("/useful/{id}")
//    public ResponseEntity<Void> deleteUseful(@PathVariable Integer id) {
//        usefulRepository.deleteById(id);
//        return ResponseEntity.ok().build();
//    }
}
