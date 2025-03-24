package io.quacker.global.config;

import io.quacker.common.dao.JwtRepository;
import io.quacker.common.dao.UserDeletionRepository;
import io.quacker.domain.auth.dao.MemoryJwtRepository;
import io.quacker.domain.user.dao.MemoryUserDeletionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    // Redis 추가시에 수정
    @Bean
    public JwtRepository jwtRepository() {
        return new MemoryJwtRepository();
    }

    @Bean
    public UserDeletionRepository userDeletionRepository() {
        return new MemoryUserDeletionRepository();
    }
}
