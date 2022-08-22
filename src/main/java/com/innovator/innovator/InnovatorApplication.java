package com.innovator.innovator;

import com.innovator.innovator.models.*;
import com.innovator.innovator.repository.ProductsRepository;
import com.innovator.innovator.repository.RoleRepository;
import com.innovator.innovator.repository.UserAuthRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.time.ZoneId;
import java.util.Collections;
import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
public class InnovatorApplication {

//    static public ZoneId ZONE_DEFAULT = TimeZone.getTimeZone("Europe/Moscow").toZoneId();

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
                registry.addMapping("/**").allowedMethods("*")
                        .allowedHeaders("*")
                        .allowedOrigins("*");
            }
        };
    }

    @Bean
    CommandLineRunner init(UserAuthRepository userAuthRepository,
                           RoleRepository roleRepository,
                           ProductsRepository productsRepository) {
        return args -> {

            if (!productsRepository.existsByType(EType.PHYSICAL))
                productsRepository.save(new Products(EType.PHYSICAL));
            if (!productsRepository.existsByType(EType.INTERNET))
                productsRepository.save(new Products(EType.INTERNET));
            if (!productsRepository.existsByType(EType.DIGITAL))
                productsRepository.save(new Products(EType.DIGITAL));
            if (!productsRepository.existsByType(EType.TECHNOLOGY))
                productsRepository.save(new Products(EType.TECHNOLOGY));

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
