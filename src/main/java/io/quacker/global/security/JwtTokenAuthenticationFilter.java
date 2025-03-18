package io.quacker.global.security;

import io.quacker.common.util.JwtTokenUtil;
import io.quacker.domain.user.dto.CustomUserDetails;
import io.quacker.domain.user.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if ( authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더 파싱
        String token = authorizationHeader.substring(7);
//        Long userId = jwtTokenUtil.extractUserId(token);
        String email = jwtTokenUtil.extractEmail(token);

        // 이메일이 존재하지않고 현재 SecurityContext에 인증 정보가 없는 경우 새로 토큰 발급
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);

            // 유효한 토근인가.
            if (jwtTokenUtil.validateToken(token, userDetails.user().getId())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null, // jwt, 생략
                                userDetails.getAuthorities());


                // 현재 요청 정보를 인증 토큰에 설정
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // done
        filterChain.doFilter(request, response);
    }
}
