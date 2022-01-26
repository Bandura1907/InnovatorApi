package com.innovator.innovator.services;

import com.innovator.innovator.MultipartUploadFile;
import com.innovator.innovator.models.User;
import com.innovator.innovator.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

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
        new MultipartUploadFile(pathPhoto).saveFile(file);
    }
}
