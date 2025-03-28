package io.quacker.domain.admin.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "관리자 게시물 관리 api", description = "어드민 API")
public interface AdminPostsApi {

    @Operation(summary = "해시태그된 게시물 삭제", description = "해당 해시태그를 가진 게시물을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제한 개수를 반환합니다", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "회원가입 성공", value = """
                            {
                              "count": "1"
                            }
                            """)
                    })
            )
    })
    ResponseEntity<?> deleteAllHashtaggedPost(
            @RequestParam("hasHashtag") String hashtagName
    );

}
