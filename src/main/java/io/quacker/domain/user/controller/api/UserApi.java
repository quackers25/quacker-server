package io.quacker.domain.user.controller.api;

import io.quacker.domain.user.dto.UserUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;

@Tag(name = "유저 API", description = "유저 조회, 수정 API")
public interface UserApi {

    //비공개 토글
    @Operation(summary ="비공개 토글" , description = "계정 비공개 여부를 토글합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "applicaion/json", examples = {
                    @ExampleObject(name = "성공", value = """
                            {
                                "isPrivate": true
                            }
                            """)
            }))
    })
    @PatchMapping("/{userId}/visibility")
    ResponseEntity<?> toggleVisibility(Long userId);

    //삭제 "요청"
    @Operation(summary ="계정 삭제 요청" , description = "계정 삭제를 요청합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "applicaion/json", examples = {
                    @ExampleObject(name = "요청 성공", value = """
                            {
                                "requestAt": "2025-03-28T02:35:07.582+00:00",
                                "deleteAt": "2025-03-28T02:36:07.582+00:00"
                            }
                            """),
            }))
    })
    ResponseEntity<?> requestDelete(Long userId);

    //삭제 취소
    @Operation(summary ="삭제요청 취소" , description = "계정삭제 중단합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "applicaion/json", examples = {
                    @ExampleObject(name = "요청 성공", value = """
                            {
                                "result: true
                            }
                            """),
            }))
    })
    ResponseEntity<?> abort(Long userId);

    // 프로필 조회
    @Operation(summary ="프로필 조회" , description = "사용자의 프로필을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "applicaion/json", examples = {
                    @ExampleObject(name = "조회 성공", value = """
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
                            """),
            })),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "applicaion/json", examples = {
                    @ExampleObject(name = "유저를 찾을 수 없음", value = """
                            {
                                "errorMessage": "유저를 찾을 수 없음",
                                "httpStatusCode": 400
                            }
                            """),
            }))
    })
    ResponseEntity<?> getProfile(Long userId);

    // 프로필 수정
    @Operation(summary ="프로필 수정" , description = "사용자의 프로필을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "applicaion/json", examples = {
                    @ExampleObject(name = "조회 성공", value = """
                            {
                                "id": 1,
                                "email": "quacker2025@gamil.com",
                                "name": "newOne",
                                "bio": null,
                                "avatarImageUrl": null,
                                "isVerified": false,
                                "isLocked": false,
                                "isPrivate": false,
                                "posts": null
                            }
                            """, description = "UserUpdateDto로 수정합니다"),
            })),
    })
    ResponseEntity<?> editProfile( Long userId, UserUpdateDto userUpdateDto);
}
