package io.quacker.global.config;

import io.quacker.common.util.JwtTokenUtil;
import io.quacker.domain.user.service.CustomUserDetailsService;
import io.quacker.global.security.JwtTokenAuthenticationFilter;
import io.quacker.global.security.JwtTokenAuthenticationProvider;
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

    private final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

    // BCrypt 인코더 추가
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))  // iframe 허용하기 위하여
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/join").permitAll()
                        .requestMatchers("/h2-console/**").permitAll() // H2 Console 접근 허용
                        .requestMatchers("/api/v1/posts/**").authenticated() // posts 관련 엔드포인트는 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                )
                .authenticationProvider(jwtTokenAuthenticationProvider)
                .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
