package com.actividad3.users_service.config;

import com.actividad3.users_service.entity.AppUser;
import com.actividad3.users_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DemoUserSeeder {

    @Bean
    CommandLineRunner seedDemoUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> userRepository.findByEmail("cliente@relatos.com")
                .orElseGet(() -> {
                    AppUser user = new AppUser();
                    user.setEmail("cliente@relatos.com");
                    user.setPasswordHash(passwordEncoder.encode("123456"));
                    user.setFullName("Cliente Demo");
                    user.setRoles(Set.of("CUSTOMER"));
                    user.setEnabled(true);
                    return userRepository.save(user);
                });
    }
}
