package com.innovator.innovator.controllers;

import com.innovator.innovator.models.Recommendation;
import com.innovator.innovator.models.ReportError;
import com.innovator.innovator.models.User;
import com.innovator.innovator.services.RecommendationService;
import com.innovator.innovator.services.ReportErrorService;
import com.innovator.innovator.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api")
public class UserController {

    private ServerProperties serverProperties;

    private UserService userService;
    private ReportErrorService reportErrorService;
    private RecommendationService recommendationService;

    @GetMapping("/photo/{name}")
    @ResponseBody
    public ResponseEntity<byte[]> getPhoto(@PathVariable String name) throws IOException {
//        File imgPath = new File("src/main/resources/static/upload/" + name);
        File imgPath = new File("/root/uploadFiles/" + name);

        byte[] image = Files.readAllBytes(imgPath.toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(image.length);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    @PostMapping("/social_auth")
    public ResponseEntity<Map<String, Object>> socialAuth(@RequestBody User userBody) {
        User user = userService.findByEmail(userBody.getEmail()).orElseGet(() -> {
            User item = new User();

            item.setEmail(userBody.getEmail());
            item.setPhotoUrl(userBody.getPhotoUrl());
            item.setFullName(userBody.getFullName());

            return userService.saveUser(item);
        });

        return new ResponseEntity<>(Map.of(
                "clientId", user.getClientId(),
                "photo", user.getPhotoUrl() == null ? "" : user.getPhotoUrl(),
                "message", "Authentication succesfull"),
                HttpStatus.CREATED);
    }

    @PostMapping("/report_error/{clientId}")
    public ResponseEntity<Map<String, String>> reportError(@PathVariable int clientId, @RequestBody ReportError reportErrorBody) {
        User user = userService.findById(clientId);
        ReportError reportError = new ReportError();

        reportError.setCustomEmail(reportErrorBody.getCustomEmail());
        reportError.setUser(user);

        reportErrorService.saveReport(reportError);

        return ResponseEntity.ok(Map.of("message", "Message sended"));
    }

    @PostMapping("/send_recommendation/{clientId}")
    public ResponseEntity<Map<String, String>> sendRecommendation(@PathVariable int clientId, @RequestBody Recommendation recommendationBody) {
        User user = userService.findById(clientId);
        Recommendation recommendation = new Recommendation();

        recommendation.setMessageText(recommendationBody.getMessageText());
        recommendation.setCustomEmail(recommendation.getCustomEmail());
        recommendation.setUser(user);

        recommendationService.saveRecommendation(recommendation);

        return ResponseEntity.ok(Map.of("message", "Message sended"));
    }

    @PostMapping("/set_profile_avatar/{clientId}")
    public ResponseEntity<Map<String, String>> setAvatar(@PathVariable int clientId, @RequestParam("avatar") MultipartFile avatar) throws IOException {
        User user = userService.findById(clientId);

        if (avatar == null || user == null) {
            return ResponseEntity.notFound().build();
        }

//        String uploadPath = "src/main/resources/static/upload";
        String uploadPath = "/root/uploadFiles";
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String nameFile = avatar.getOriginalFilename();

        String absolutePath = new File(uploadPath).getAbsolutePath();
//        avatar.transferTo(new File(absolutePath + "\\" + nameFile));
        avatar.transferTo(new File(absolutePath + "/" + nameFile));

        user.setPhotoUrl("http://" + InetAddress.getLoopbackAddress().getHostAddress() + ":" + serverProperties.getPort() +
                "/api/photo/" + nameFile);

        userService.saveUser(user);

        return ResponseEntity.ok(Map.of("message", "Avatar updated"));
    }
}
