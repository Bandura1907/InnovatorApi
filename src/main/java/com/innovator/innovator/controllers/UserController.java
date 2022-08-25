package com.innovator.innovator.controllers;

import com.innovator.innovator.HelpfullyService;
import com.innovator.innovator.MultipartUploadFile;
import com.innovator.innovator.models.User;
import com.innovator.innovator.payload.request.DonateRequest;
import com.innovator.innovator.payload.response.MessageResponse;
import com.innovator.innovator.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {
    private static final String DEFAULT_PHOTO =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlf91yfOT2B7vCu4ikHj54dlXtsCAo7ZzeCw&usqp=CAU";
    private final UserService userService;

    @Value("${upload.path.user.photo}")
    private String pathPhoto;

    @Autowired
    public UserController( UserService userService) {
//        this.serverProperties = serverProperties;
        this.userService = userService;
    }

    @GetMapping("/photo/{name}")
    @ResponseBody
    public ResponseEntity<byte[]> getPhoto(@PathVariable String name) throws ExecutionException, InterruptedException {
        MultipartUploadFile image = new MultipartUploadFile(pathPhoto + name);
        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                .contentType(MediaType.IMAGE_JPEG).body(image.getPhotoFile().get());
    }

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
            user.setPhotoUrl(DEFAULT_PHOTO);

        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/update_user/{clientId}")
    public ResponseEntity<User> updateUser(@PathVariable int clientId, @RequestBody User userBody) {
        User user = userService.findById(clientId);
        user.setEmail(userBody.getEmail());
        user.setFullName(userBody.getFullName());

        if (userBody.getPhotoUrl() == null)
            user.setPhotoUrl(DEFAULT_PHOTO);
        else
            user.setPhotoUrl(userBody.getPhotoUrl());

        return ResponseEntity.ok(userService.saveUser(user));
    }

    @DeleteMapping("/delete_user/{clientId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int clientId) {
        userService.deleteUserById(clientId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/social_auth")
    public ResponseEntity<Map<String, Object>> socialAuth(@RequestBody User userBody) {
        User user = userService.findByEmail(userBody.getEmail()).orElseGet(() -> {
            User item = new User();

            item.setEmail(userBody.getEmail());
            item.setPhotoUrl(userBody.getPhotoUrl() == null ? DEFAULT_PHOTO :
                    userBody.getPhotoUrl());
            item.setFullName(userBody.getFullName());

            return userService.saveUser(item);
        });

        return new ResponseEntity<>(Map.of(
                "clientId", user.getClientId(),
                "fullName", user.getFullName(),
                "photo", user.getPhotoUrl() == null ? "" : user.getPhotoUrl(),
                "message", "Authentication successful"),
                HttpStatus.OK);
    }

    @PostMapping("/add_donate/{id}")
    public ResponseEntity<?> addDonate(@RequestBody DonateRequest donateRequest, @PathVariable int id) {
        User user = userService.findById(id);
        user.setDonate(donateRequest.getSum());
        userService.saveUser(user);
        return ResponseEntity.ok(new MessageResponse("Donate add to user (" + user.getDonate() + ")"));
    }

    @PostMapping(value = "/set_profile_avatar/{clientId}")
    public ResponseEntity<Map<String, String>> setAvatar(@PathVariable int clientId, @RequestParam("avatar") MultipartFile avatar) throws IOException {
        User user = userService.findById(clientId);

        if (avatar == null || user == null) {
            return ResponseEntity.notFound().build();
        }
        userService.saveUserPhoto(avatar);
        user.setPhotoUrl(HelpfullyService.getCurrentBaseUrl() + "/api/photo/" + avatar.getOriginalFilename());
        userService.saveUser(user);

        return ResponseEntity.ok(Map.of("message", "Avatar updated",
                                        "photoUrl", user.getPhotoUrl()));
    }

}
