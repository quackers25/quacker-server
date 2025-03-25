package io.quacker.global.config;

import io.quacker.global.security.JwtAuthenticationFilter;
import io.quacker.global.security.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // BCrypt 인코더 추가
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationProvider jwtTokenAuthenticationProvider) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))  // iframe 허용하기 위하여
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/h2-console/**").permitAll() // H2 Console 접근 허용                                .anyRequest().authenticated()
                                .requestMatchers("/api/v1/auth/login", "/api/v1/auth/join", "/api/v1/auth/refresh").permitAll()
                                .requestMatchers("/api/v1/auth/logout").authenticated()
                                .anyRequest().authenticated()
//                                .anyRequest().permitAll()
                )
                .authenticationProvider(jwtTokenAuthenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
