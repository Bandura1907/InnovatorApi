package com.innovator.innovator;

import com.innovator.innovator.models.ERole;
import com.innovator.innovator.models.Role;
import com.innovator.innovator.models.UserAuth;
import com.innovator.innovator.repository.RoleRepository;
import com.innovator.innovator.repository.UserAuthRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

@SpringBootApplication
public class InnovatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnovatorApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserAuthRepository userAuthRepository, RoleRepository roleRepository) {
        return args -> {
              if (!userAuthRepository.existsByUsername("innovator")) {
                  Role role = new Role();
                  role.setName(ERole.ROLE_ADMIN);
                  roleRepository.save(role);

                  UserAuth userAdmin = new UserAuth();
                  userAdmin.setUsername("innovator");
                  userAdmin.setPassword(new BCryptPasswordEncoder().encode("zsxadc1234"));
                  userAdmin.setRoles(Collections.singleton(role));
                  userAuthRepository.save(userAdmin);
              }

            if (!userAuthRepository.existsByUsername("manager")) {
                Role role = new Role();
                role.setName(ERole.ROLE_MANAGER);
                roleRepository.save(role);

                UserAuth userAdmin = new UserAuth();
                userAdmin.setUsername("manager");
                userAdmin.setPassword(new BCryptPasswordEncoder().encode("manager1234"));
                userAdmin.setRoles(Collections.singleton(role));
                userAuthRepository.save(userAdmin);
            }
        };
    }
}
