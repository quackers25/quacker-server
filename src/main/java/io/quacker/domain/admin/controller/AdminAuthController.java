package io.quacker.domain.admin.controller;


import io.quacker.domain.admin.controller.api.AdminAuthApi;
import io.quacker.domain.admin.dto.AdminCreateDto;
import io.quacker.domain.admin.dto.AdminLoginDto;
import io.quacker.domain.admin.service.AdminService;
import io.quacker.domain.auth.dto.JwtTokens;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admins/auth")
@RequiredArgsConstructor

public class AdminAuthController implements AdminAuthApi {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginDto adminLoginDto) {
        JwtTokens tokens = adminService.login(adminLoginDto);

        // 쿠키 생성
        ResponseCookie accessTokenCookie = ResponseCookie.from("adminAccessToken", tokens.accessToken())
                .httpOnly(true)
//                .secure(true)  // HTTPS에서만 전송
                .path("/")     // 쿠키 경로 설정
                .maxAge(15 * 60) // 쿠키 만료시간 (7일)
//                .sameSite("Strict") // Cross-site 요청 제한
                .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("adminRefreshToken", tokens.refreshToken())
                .httpOnly(true)
//                .secure(true)
                .path("/api/v1/admins/auth")
                .maxAge(14 * 24 * 60 * 60)
//                .sameSite("Strict")
                .build();

        // ResponseEntity에 헤더와 응답 본문 설정
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(adminService.getAdminById(tokens.userId()));
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody AdminCreateDto adminCreateDto) {
        adminService.join(adminCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("result", true));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue("adminRefreshToken") String refreshToken
    ) {
        JwtTokens tokens = adminService.refresh(refreshToken);
        ResponseCookie accessTokenCookie = ResponseCookie.from("adminAccessToken", tokens.accessToken())
                .httpOnly(true)
                .path("/")
                .maxAge(15 * 60) // 쿠키 만료시간 (7일)
                .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("adminRefreshToken", tokens.refreshToken())
                .httpOnly(true)
                .path("/api/v1/admins/auth")
                .maxAge(14 * 24 * 60 * 60)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(Map.of("result", true));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = "adminAccessToken") String accessToken,
            @CookieValue(value = "adminRefreshToken") String refreshToken

    ) {
        adminService.logout(accessToken, refreshToken);

        ResponseCookie access = ResponseCookie.from("adminAccessToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();

        ResponseCookie refresh = ResponseCookie.from("adminRefreshToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, access.toString())
                .header(HttpHeaders.SET_COOKIE, refresh.toString())
                .body(Map.of("result", true));
    }
}
