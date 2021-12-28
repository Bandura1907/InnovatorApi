package com.innovator.innovator.controllers;

import com.innovator.innovator.models.User;
import com.innovator.innovator.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class UserController {

    private ServerProperties serverProperties;
    private UserService userService;

    @GetMapping("/photo/{name}")
    @ResponseBody
    public ResponseEntity<byte[]> getPhoto(@PathVariable String name) throws IOException {
//        return getMedia("src/main/resources/static/upload/" + name);
        return getMedia("/root/uploadFiles/" + name);
    }

//    @GetMapping("/getDefaultPhoto")
//    @ResponseBody
//    public ResponseEntity<byte[]> getDefaultPhoto () throws IOException {
//        return getMedia("");
//    }

    @GetMapping("/all_users")
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/user_by_id/{clientId}")
    public ResponseEntity<User> userById(@PathVariable int clientId) {
        return ResponseEntity.ok(userService.findById(clientId));
    }

    @PostMapping("/add_user")
    public ResponseEntity<User> addUser(@RequestBody User user) {

        if (user.getPhotoUrl() == null)
            user.setPhotoUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlf91yfOT2B7vCu4ikHj54dlXtsCAo7ZzeCw&usqp=CAU");

        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/update_user/{clientId}")
    public ResponseEntity<User> updateUser(@PathVariable int clientId, @RequestBody User userBody) {
        User user = userService.findById(clientId);
        user.setEmail(userBody.getEmail());
        user.setFullName(userBody.getFullName());

        if (userBody.getPhotoUrl() == null)
            user.setPhotoUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlf91yfOT2B7vCu4ikHj54dlXtsCAo7ZzeCw&usqp=CAU");
        else
            user.setPhotoUrl(userBody.getPhotoUrl());

        return ResponseEntity.ok(userService.saveUser(user));
    }

    @DeleteMapping("/delete_user/{clientId}")
    public ResponseEntity<String> deleteUser(@PathVariable int clientId) {
        userService.deleteUserById(clientId);
        return ResponseEntity.ok("Delete user " + clientId);
    }

    @PostMapping("/social_auth")
    public ResponseEntity<Map<String, Object>> socialAuth(@RequestBody User userBody) {
        User user = userService.findByEmail(userBody.getEmail()).orElseGet(() -> {
            User item = new User();

            item.setEmail(userBody.getEmail());
            item.setPhotoUrl(userBody.getPhotoUrl() == null ? "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlf91yfOT2B7vCu4ikHj54dlXtsCAo7ZzeCw&usqp=CAU" :
                    userBody.getPhotoUrl());
            item.setFullName(userBody.getFullName());

            return userService.saveUser(item);
        });

        return new ResponseEntity<>(Map.of(
                "clientId", user.getClientId(),
                "fullName", user.getFullName(),
                "photo", user.getPhotoUrl() == null ? "" : user.getPhotoUrl(),
                "message", "Authentication succesfull"),
                HttpStatus.OK);
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


        String uuidFile = UUID.randomUUID().toString();
        String nameFile = uuidFile + avatar.getOriginalFilename();

        String absolutePath = new File(uploadPath).getAbsolutePath();
//        avatar.transferTo(new File(absolutePath + "\\" + nameFile));
        avatar.transferTo(new File(absolutePath + "/" + nameFile));

//        user.setPhotoUrl("http://localhost" + ":" + serverProperties.getPort() + "/api/photo/" + nameFile);
        user.setPhotoUrl("http://65.108.182.146" + ":" + serverProperties.getPort() + "/api/photo/" + nameFile);

        userService.saveUser(user);

        return ResponseEntity.ok(Map.of("message", "Avatar updated",
                                        "photoUrl", user.getPhotoUrl()));
    }

    private ResponseEntity<byte[]> getMedia(String path) throws IOException {
        try {
            File imgPath = new File(path);

            byte[] image = Files.readAllBytes(imgPath.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(image.length);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (NoSuchFileException ex) {
            log.error("error reading file: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
