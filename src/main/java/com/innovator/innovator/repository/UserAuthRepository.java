package com.innovator.innovator.repository;

import com.innovator.innovator.models.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, Integer> {

    Optional<UserAuth> findByUsername(String username);
    Boolean existsByUsername(String username);
}
