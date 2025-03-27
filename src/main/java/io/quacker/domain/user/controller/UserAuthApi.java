package io.quacker.domain.user.controller;

import io.quacker.domain.auth.dto.JwtTokens;
import io.quacker.domain.user.dto.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "User Auth", description = "유저 인증 관련 API")
public interface UserAuthApi {

    /**
     * <h4>로그인</h4>
     * @param userLoginDto
     * @param errors
     * @return
     */
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
                @ExampleObject(name = "유저 로그인 성공", value = """
                        {
                            "email" : "quacker2025@gmail.com",
                            "password" : "passworD1!"
                        }
                """),})),
        @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
                @ExampleObject(name = "팔로잉 실패 - 정보누락/잘못된 입력", value = """
                        {
                            "email" : "quacker2025@gmail.com",
                        }
                """)}))})
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody @Valid UserLoginDto userLoginDto, Errors errors);

    /**
     * 가입
     * @param userCreateDto
     * @param errors
     * @return ResponseEntity
     */
    @PostMapping("/join")
    ResponseEntity<?> join(@RequestBody @Valid UserCreateDto userCreateDto, Errors errors);

    /**
     * 리프레시 토큰으로 액세스토큰 재발급
     * @param refreshToken
     * @return
     */
    @PostMapping("/refresh")
    ResponseEntity<?> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    );

    /**
     * 소유한 토큰 모두 만료 및 쿠키 삭제
     * @return
     */
    @PostMapping("{userId}/logout")
    ResponseEntity<?> logout(
            @PathVariable("userId") Long userId,
            @CookieValue(value = "accessToken") String accessToken,
            @CookieValue(value = "refreshToken") String refreshToken

    );

    //힌트로 이메일 찾기
    @GetMapping("/hint")
    ResponseEntity<?> getEmailhint(@RequestBody UserHintDto userHintDto);

    // 이메일 중복 확인?
    @PostMapping("/duplicate-email")
    ResponseEntity<?> duplicateEmail(@RequestBody UserEmailDto userEmailDto);

    // 사용자 이름 중복 확인
    @GetMapping("/username/{username}")
    ResponseEntity<?> duplicateUsername(@RequestBody UserDto userDto);

    // 이메일 인증 발송
    @PostMapping("/send-code")
    ResponseEntity<?> createEmailSession(@RequestBody UserEmailDto userEmailDto);

    // 현재 캐시에 저장된 코드와 비교
    @PutMapping("/verify-email")
    ResponseEntity<?> verifyEmail(@RequestBody UserEmailCodeDto userEmailCodeDto);


    // 사용자 비밀번호 변경(인증 세션 필요)
    @PutMapping("/reset-password")
    ResponseEntity<?> resetPassword(@RequestBody UserResetPasswordDto userResetPasswordDto);
}
