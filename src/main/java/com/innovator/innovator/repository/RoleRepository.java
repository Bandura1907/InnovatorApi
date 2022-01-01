package com.innovator.innovator.repository;

import com.innovator.innovator.models.ERole;
import com.innovator.innovator.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole name);
}
