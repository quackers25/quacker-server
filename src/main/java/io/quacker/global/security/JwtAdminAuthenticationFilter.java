package io.quacker.global.security;

import io.quacker.common.util.JwtTokenUtil;
import io.quacker.domain.admin.dto.AdminDetails;
import io.quacker.domain.admin.service.AdminDetailsService;
import io.quacker.domain.auth.service.JwtBlacklistService;
import io.quacker.domain.user.dto.CustomUserDetails;
import io.quacker.domain.user.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class JwtAdminAuthenticationFilter extends OncePerRequestFilter {

    private final AdminDetailsService adminDetailsService;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        // 허용된 URI는 인증시도 하지않음
        // 추후 admin과 user이 도메인 분리된다면 사용할 것
//        String uri = request.getRequestURI();
//        if (!uri.startsWith("/api/v1/admins")) {
//            filterChain.doFilter(request, response);
//            return;
//        }


        // 인증 헤더 추출
        String authorizationHeader = request.getHeader("Authorization");

        // 인증 헤더 값 검사
        if ( authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더 파싱
        String token = authorizationHeader.substring(7);

        // 엑세스 토큰 만효 확인
        if (jwtTokenUtil.isTokenExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 사용자 email(username) 추출
        String username = jwtTokenUtil.extractEmail(token);

        // 어드민 토큰이 아니라면
        if (!jwtTokenUtil.extractRole(token).equals("admin")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 새 토큰 발급, when 유저이름이 존재하지않고 현재 SecurityContext에 인증 정보가 없는 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            AdminDetails details = (AdminDetails) adminDetailsService.loadUserByUsername(username);
            log.debug(String.format("User loaded name of [%s]", details.getUsername()));
            // 유효한 토근인가.
            if (jwtTokenUtil.validateAccessToken(token, details.getId()) &&
                    jwtBlacklistService.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                details,
                                null, // jwt, 생략
                                details.getAuthorities());

                // 현재 요청 정보를 인증 토큰에 설정
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
