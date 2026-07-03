package config;

import java.util.UUID;

import annotations.Bean;
import annotations.Configuration;
import models.User;
import repository.Repository;
import repository.UserRepository;

@Configuration
public class AppConfig {
    @Bean
    public UserRepository<UUID, User> userRepository(){
        return new Repository<>();
    }
}
