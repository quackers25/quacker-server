package io.quacker.global.security;

import io.quacker.common.util.JwtTokenUtil;
import io.quacker.domain.auth.service.JwtBlacklistService;
import io.quacker.domain.user.dto.CustomUserDetails;
import io.quacker.domain.user.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtTokenUtil jwtTokenUtil;
    private static final Set<String> WHITELIST = Set.of(
            "/api/v1/auth/login",
            "/api/v1/auth/join",
            "/api/v1/auth/refresh"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        // 허용된 URI는 인증시도 하지않음
        String uri = request.getRequestURI();
        if(WHITELIST.stream().anyMatch(uri::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 인증 헤더 추출
        String authorizationHeader = request.getHeader("Authorization");

        // 인증 헤더 값 검사
        if ( authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("헤더 없음");
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더 파싱
        String token = authorizationHeader.substring(7);

        // 엑세스 토큰 만효 확인
        if (jwtTokenUtil.isTokenExpired(token)) {
            log.info("토큰 만료");
            filterChain.doFilter(request, response);
            return;
        }

        // 사용자 email 추출
        String email = jwtTokenUtil.extractEmail(token);

        // 새 토큰 발급, when 이메일이 존재하지않고 현재 SecurityContext에 인증 정보가 없는 경우
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
            log.debug(String.format("User loaded name of [%s]", userDetails.getUsername()));
            // 유효한 토근인가.
            if (jwtTokenUtil.validateAccessToken(token, userDetails.getUserId()) &&
                    jwtBlacklistService.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        //Todo : jwt 커스텀 토큰 사용? -> AuthenticationManager 위임 필요
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null, // jwt, 생략
                                userDetails.getAuthorities());


                // 현재 요청 정보를 인증 토큰에 설정
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
