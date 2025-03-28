package io.quacker.domain.admin.controller.api;

import io.quacker.domain.admin.dto.AdminCreateDto;
import io.quacker.domain.admin.dto.AdminLoginDto;
import io.quacker.domain.auth.dto.JwtTokens;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "관리자 인증 API", description = "관리자 인증 관련 API")
public interface AdminAuthApi {

    @Operation(summary = "로그인", description = "로그인을 수행합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "사용자 로그인 성공, 토큰이 해더로 발급됩니다.",
                    headers = {
                            @Header(name = "adminAccessToken", description = "엑세스 토큰", schema = @Schema(type = "string")),
                            @Header(name = "adminRefreshToken", description = "리프레시 토큰", schema = @Schema(type = "string"))
                    },
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "유저 로그인 성공", value = """
                                    {
                                        "result": true
                                    }
                                    """)})
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "유저를 찾을 수 없음", value = """
                                    {
                                        "errorMessage": "관리자를 찾을 수 없음",
                                        "httpStatusCode": 404
                                    }
                                    """
                            ),
                            @ExampleObject(name = "비밀번호가 일치하지않음", value = """
                                    {
                                        "errorMessage": "비밀번호가 일치하지 않음",
                                        "httpStatusCode": 400
                                    }
                                    """
                            )
                    })),
    })
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody AdminLoginDto adminLoginDto);

    @Operation(summary = "가입", description = "회원가입을 수행합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "회원가입 성공", value = """
                            {
                              "result": true
                            }
                            """),})),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "유저 로그인 실패", value = """
                            {
                                "errorMessage": "Bad request. Invalid authentication."
                            }
                            """),
                    @ExampleObject(name = "확인 비밀번호 불일치", value = """
                            {
                                "errorMessage": "비밀번호가 일치하지 않음",
                                "httpStatusCode": 400
                            }
                            """),
            })),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "존재하지 않는 유저", value = """
                        {
                             "errorMessage": "관리자를 찾을 수 없음",
                             "httpStatusCode": 404
                        }
                        """)
            })),
            @ApiResponse(responseCode = "409", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "이메일 중복", value = """
                        {
                            "errorMessage": "사용할 수 없는 이메일",
                            "httpStatusCode": 409
                        }
                        """)}
            ))})
    @PostMapping("/join")
    ResponseEntity<?> join(@RequestBody AdminCreateDto adminCreateDto);

    @Operation(summary = "토큰 재발급", description = "리프레시토큰을 블랙리스트에 등록하고, 두 토큰을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "재발급 성공, 토큰이 해더로 발급됩니다.",
                    headers = {
                            @Header(name = "adminAccessToken", description = "엑세스 토큰", schema = @Schema(type = "string")),
                            @Header(name = "adminRefreshToken", description = "리프레시 토큰", schema = @Schema(type = "string"))
                    },
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name="발급성공", value = """
                                    {
                                        "result": true
                                    }
                                    """
                            )
                    })
            )
    })
    @PostMapping("/refresh")
    ResponseEntity<?> refresh(
            @CookieValue("adminRefreshToken") String refreshToken
    );

    @Operation(summary = "로그아웃", description = "쿠키로 소지중인 토큰을 모두 만료시킵니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "중복", value = """
                            {
                              "result": true
                            }
                            """)
            })),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "중복", value = """
                            {
                                "message": "Bad request. Invalid authentication."
                            }
                            """)
            }))
    })
    @PostMapping("/logout")
    ResponseEntity<?> logout(
            @CookieValue(value = "adminAccessToken") String accessToken,
            @CookieValue(value = "adminRefreshToken") String refreshToken

    );
}
