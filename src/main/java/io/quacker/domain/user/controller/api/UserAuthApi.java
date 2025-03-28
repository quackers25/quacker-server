package io.quacker.domain.user.controller.api;

import io.quacker.domain.user.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 인증 API", description = "유저 인증 관련 API")
public interface UserAuthApi {

    /**
     * <h4>로그인</h4>
     * @param userLoginDto
     * @param errors
     * @return
     */
    @Operation(summary = "로그인", description = "로그인을 수행합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "사용자 로그인 성공, 토큰이 해더로 발급됩니다.",
                    headers = {
                            @Header(name = "accessToken", description = "엑세스 토큰", schema = @Schema(type = "string")),
                            @Header(name = "refreshToken", description = "리프레시 토큰", schema = @Schema(type = "string"))
                    },
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "유저 로그인 성공", value = """
                                    {
                                        "id": 1,
                                        "email": "quacker2025@gamil.com",
                                        "name": "Username",
                                        "bio": "Hello world!",
                                        "avatarImageUrl": "https://img.icons8.com/color-pixels/32/duck.png",
                                        "isVerified": false,
                                        "isLocked": false,
                                        "isPrivate": false,
                                        "posts": null
                                    }
                                    """)})
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "유저를 찾을 수 없음", value = """
                                    {
                                        "errorMessage": "유저를 찾을 수 없음",
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
    ResponseEntity<?> login(@RequestBody @Valid UserLoginDto userLoginDto, Errors errors);

    /**
     * 가입
     * @param userCreateDto
     * @param errors
     * @return ResponseEntity
     */
    @Operation(summary = "가입", description = "회원가입을 수행합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "회원가입 성공", value = """
                            {
                              "id": 1,
                              "email": "quacker2025@gamil.com",
                              "name": "Username",
                              "bio": "Hello world!",
                              "avatarImageUrl": "https://img.icons8.com/color-pixels/32/duck.png",
                              "isVerified": false,
                              "isLocked": false,
                              "isPrivate": false,
                              "posts": null
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
                             "errorMessage": "유저를 찾을 수 없음",
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
    ResponseEntity<?> join(@RequestBody @Valid UserCreateDto userCreateDto, Errors errors);

    /**
     * 소유한 토큰 모두 만료 및 쿠키 삭제
     * @return
     */
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
    @PostMapping("{userId}/logout")
    ResponseEntity<?> logout(
            @PathVariable("userId") Long userId,
            @CookieValue(value = "accessToken") String accessToken,
            @CookieValue(value = "refreshToken") String refreshToken

    );

    /**
     * 리프레시 토큰으로 액세스토큰 재발급
     * @param refreshToken
     * @return
     */
    @Operation(summary = "토큰 재발급", description = "리프레시토큰을 블랙리스트에 등록하고, 두 토큰을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "재발급 성공, 토큰이 해더로 발급됩니다.",
                    headers = {
                            @Header(name = "accessToken", description = "엑세스 토큰", schema = @Schema(type = "string")),
                            @Header(name = "refreshToken", description = "리프레시 토큰", schema = @Schema(type = "string"))
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
            @CookieValue(value = "refreshToken") String refreshToken
    );

    //힌트로 이메일 찾기
    @Operation(summary = "힌트 보기", description = "가입시 기입한 힌트로 마스킹된 이메일을 제공합니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "회원가입 성공", value = """
                            {
                              "result": "quac****@gamil.com"
                            }
                            """)

            })),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "일치하는 힌트가 없음", value = """
                            {
                                "errorMessage": "유저를 찾을 수 없음",
                                "httpStatusCode": 404
                            }
                            """)
            }))
    })
    @GetMapping("/hint")
    ResponseEntity<?> getEmailhint(@RequestBody UserHintDto userHintDto);

    // 이메일 중복 확인
    @Operation(summary = "이메일 중복 확인", description = "이메일의 중복여부를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "중복", value = """
                            {
                              "result": true
                            }
                            """),
                    @ExampleObject(name = "중복이 아님", value = """
                            {
                              "result": false
                            }
                            """)
            }))
    })
    @PostMapping("/duplicate-email")
    ResponseEntity<?> duplicateEmail(@RequestBody UserEmailDto userEmailDto);

    // 사용자 이름 중복 확인
    @Operation(summary = "사용자 이름 중복 확인", description = "사용자 이름의 중복여부를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "중복", value = """
                            {
                              "result": true
                            }
                            """),
                    @ExampleObject(name = "중복이 아님", value = """
                            {
                              "result": false
                            }
                            """)
            }))
    })
    @GetMapping("/username/{username}")
    ResponseEntity<?> duplicateUsername(@RequestBody UserDto userDto);

    // 이메일 인증 발송
    @Operation(summary = "이메일 인증 발송", description = "사용자 인증용 코드를 발송하고 서버가 보관합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "중복", value = """
                            {
                                "sentAT": "2025-03-27T05:00:52.381+00:00"
                            }
                            """),

            })),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "존재하지 않는 유저", value = """
                            {
                                "errorMessage": "유저를 찾을 수 없음",
                                "httpStatusCode": 404
                            }
                            """),

            }))
    })
    @PostMapping("/send-code")
    ResponseEntity<?> createEmailSession(@RequestBody UserEmailDto userEmailDto);

    // 현재 캐시에 저장된 코드와 비교
    @Operation(summary = "코드 인증", description = "인증 코드를 검증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "인증성공", value = """
                            {
                                "isVerified": true
                            }
                            """),

            })),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "토큰 누락/코드 불일치", value = """
                            {
                                "message": "Bad request. Invalid authentication."
                            }
                            """),

            }))
    })
    @PutMapping("/verify-email")
    ResponseEntity<?> verifyEmail(@RequestBody UserEmailCodeDto userEmailCodeDto);


    // 사용자 비밀번호 변경(인증 세션 필요)
    @Operation(summary = "코드 인증", description = "인증 코드를 검증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "인증성공", value = """
                            {
                                "isVerified": true
                            }
                            """),

            })),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "코드 불일치", value = """
                            {
                                "errorMessage": "코드 인증 실패",
                                "httpStatusCode": 400
                            }
                            """),
                    @ExampleObject(name = "만료된 인증코드/인증을 찾을 수 없음", value = """
                            {
                                "errorMessage": "만료되거나 존재하지않은 인증",
                                "httpStatusCode": 400
                            }
                            """),

            })),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "토큰 누락", value = """
                            {
                                "message": "Bad request. Invalid authentication."
                            }
                            """),

            }))
    })
    @PutMapping("/reset-password")
    ResponseEntity<?> resetPassword(@RequestBody UserResetPasswordDto userResetPasswordDto);
}
