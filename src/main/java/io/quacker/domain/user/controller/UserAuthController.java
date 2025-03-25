package io.quacker.domain.user.controller;

import io.quacker.domain.auth.dto.JwtTokens;
import io.quacker.domain.user.dto.UserCreateDto;
import io.quacker.domain.user.dto.UserLoginDto;
import io.quacker.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    private final UserService userService;
//    private @Value("${jwt.expiration.}") int cookieExpiration; // 7 * 24 * 60 * 60

    /**
     * 로그인 엔드포인트
     * @param userLoginDto
     * @param errors
     * @return ResponseEntity
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDto userLoginDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
        }
        JwtTokens tokens = userService.login(userLoginDto);
        // 쿠키 생성
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", tokens.accessToken())
                .httpOnly(true)
//                .secure(true)  // HTTPS에서만 전송
                .path("/")     // 쿠키 경로 설정
                .maxAge(15 * 60) // 쿠키 만료시간 (7일)
//                .sameSite("Strict") // Cross-site 요청 제한
                .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
                .httpOnly(true)
//                .secure(true)
                .path("/api/v1/auth")
                .maxAge(14 * 24 * 60 * 60)
//                .sameSite("Strict")
                .build();


        // ResponseEntity에 헤더와 응답 본문 설정
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }

    /**
     * 가입
     * @param userCreateDto
     * @param errors
     * @return ResponseEntity
     */
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserCreateDto userCreateDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.join(userCreateDto));
    }

    /**
     * 리프레시 토큰으로 액세스토큰 재발급
     * @param refreshToken
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        JwtTokens tokens = userService.refresh(refreshToken);
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", tokens.accessToken())
                .httpOnly(true)
                .path("/")
                .maxAge(15 * 60) // 쿠키 만료시간 (7일)
                .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
                .httpOnly(true)
                .path("/api/v1/auth")
                .maxAge(14 * 24 * 60 * 60)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }

    /**
     * 소유한 토큰 모두 만료 및 쿠키 삭제
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = "accessToken") String accessToken,
            @CookieValue(value = "refreshToken") String refreshToken

    ) {
        userService.logout(accessToken, refreshToken);

        ResponseCookie access = ResponseCookie.from("accessToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();

        ResponseCookie refresh = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, access.toString())
                .header(HttpHeaders.SET_COOKIE, refresh.toString())
                .body("로그아웃성공");
    }
}
