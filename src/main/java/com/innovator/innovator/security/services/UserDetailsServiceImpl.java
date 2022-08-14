package com.innovator.innovator.security.services;

import com.innovator.innovator.models.User;
import com.innovator.innovator.models.UserAuth;
import com.innovator.innovator.repository.UserAuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserAuthRepository userAuthRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuth userAuth = userAuthRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return userAuth;
    }

    public Optional<UserAuth> findByUsername(String username) {
        return userAuthRepository.findByUsername(username);
    }

    public Optional<UserAuth> findById(Integer id) {
        return userAuthRepository.findById(id);
    }

    public boolean existsByUsername(String username) {
        return userAuthRepository.existsByUsername(username);
    }

    public void save(UserAuth userAuth) {
        userAuthRepository.save(userAuth);
    }

    public void deleteById(Integer id) {
        userAuthRepository.deleteById(id);
    }

    public List<UserAuth> findAll() {
        return userAuthRepository.findAll();
    }
}
