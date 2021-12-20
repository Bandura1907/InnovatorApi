package com.innovator.innovator.services;

import com.innovator.innovator.models.User;
import com.innovator.innovator.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

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
}
