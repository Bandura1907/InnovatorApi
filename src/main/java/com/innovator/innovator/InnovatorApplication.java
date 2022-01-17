package com.innovator.innovator;

import com.innovator.innovator.models.ERole;
import com.innovator.innovator.models.Role;
import com.innovator.innovator.models.UserAuth;
import com.innovator.innovator.repository.RoleRepository;
import com.innovator.innovator.repository.UserAuthRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.content.commons.repository.Store;
import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.content.rest.StoreRestResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.util.Collections;

@SpringBootApplication
public class InnovatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnovatorApplication.class, args);
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofKilobytes(10485760)); //10GB
        factory.setMaxRequestSize(DataSize.ofKilobytes(10485760)); //10GB
        return factory.createMultipartConfig();
    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*");
            }
        };
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
