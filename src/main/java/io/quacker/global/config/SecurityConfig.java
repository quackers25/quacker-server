package io.quacker.global.config;

import io.quacker.global.security.JwtAdminAuthenticationFilter;
import io.quacker.global.security.JwtAuthenticationFilter;
import io.quacker.global.security.JwtAuthenticationProvider;
import jakarta.servlet.http.HttpServletResponse;
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

    private static final String[] WITHE_LIST =
        {"/h2-console/**", "/api/v1/auth/**", "/api/v1/admins/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**"};

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAdminAuthenticationFilter jwtAdminAuthenticationFilter;

    // BCrypt 인코더 추가
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 체인 설정
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthenticationProvider jwtTokenAuthenticationProvider
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))  // iframe 허용하기 위하여
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(WITHE_LIST).permitAll()
                                .requestMatchers("/api/v1/auth/logout").authenticated()
                                .requestMatchers("/api/v1/admins").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .authenticationProvider(jwtTokenAuthenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAdminAuthenticationFilter, JwtAuthenticationFilter.class)
                .exceptionHandling(e->e
                        .authenticationEntryPoint((req, res, accessDeniedException) -> {
                            res.setContentType("application/json");
                            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            res.getWriter().write("{\"message\": \"Bad request. Invalid authentication.\"}");
                        })
                        .accessDeniedHandler((req, res, accessDeniedException) -> {
                            res.setContentType("application/json");
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                            res.getWriter().write("{\"message\": \"권한이 없습니다.\"}");
                        })
                )
                // TODO: SignatureException 핸들링 추가할 것
            .build();
    }


}
