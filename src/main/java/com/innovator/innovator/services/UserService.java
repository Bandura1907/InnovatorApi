package com.innovator.innovator.services;

import com.innovator.innovator.MultipartUploadFile;
import com.innovator.innovator.models.User;
import com.innovator.innovator.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private UserRepository userRepository;

    @Value("${upload.path.user.photo}")
    private String pathPhoto;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String e) {
        return userRepository.findByEmail(e);
    }

    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    public User saveUser(User user) {
         return userRepository.save(user);
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    public void saveUserPhoto(MultipartFile file) throws FileUploadException {
        MultipartUploadFile uploadFile = new MultipartUploadFile(pathPhoto);
        uploadFile.saveFile(file);
    }
}
