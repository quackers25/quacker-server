package io.quacker.domain.user.controller.api;

import io.quacker.domain.user.dto.UserUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;

public interface UserApi {

    //비공개 토글
    @Operation(summary ="비공개 토글" , description = "계정 비공개 여부를 토글합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "applicaion/json", examples = {
                    @ExampleObject(name = "성공", value = """
                            {
                                "result": true
                            }
                            """)
            }))
    })
    @PatchMapping("/{userId}/visibility")
    ResponseEntity<?> toggleVisibility(Long userId);

    //삭제 "요청"
    @Operation(summary ="계정 삭제 요청" , description = "계정 삭제를 요청합니다.")
    @ApiResponses({
            @ApiResponse()
    })
    ResponseEntity<?> requestDelete(Long userId);

    //삭제 취소
    @Operation(summary ="삭제요청 취소" , description = "계정삭제 중단합니다.")
    @ApiResponses({
            @ApiResponse()
    })
    ResponseEntity<?> abort(Long userId);

    // 프로필 조회
    @Operation(summary ="프로필 조회" , description = "사용자의 프로필을 조회합니다.")
    @ApiResponses({
            @ApiResponse()
    })
    ResponseEntity<?> getProfile(Long userId);

    // 프로필 수정
    @Operation(summary ="프로필 수정" , description = "사용자의 프로필을 수정합니다.")
    @ApiResponses({
            @ApiResponse()
    })
    ResponseEntity<?> editProfile( Long userId, UserUpdateDto userUpdateDto);
}
