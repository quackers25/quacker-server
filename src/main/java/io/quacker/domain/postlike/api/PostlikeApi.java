package io.quacker.domain.postlike.api;

import io.quacker.domain.postlike.dto.PostLikeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시글 좋아요 API", description = "게시글 좋아요를 관리하는 API입니다.")
@RequestMapping("/api/v1/posts/{postId}/likes")
public interface PostlikeApi {

    @Operation(
            summary = "게시글 좋아요",
            description = "특정 게시글에 좋아요를 추가합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "좋아요 추가 완료", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "좋아요 성공", value = """
                            {
                              "id": 1,
                              "postId": 3,
                              "userId": 2
                            }
                            """)
            })),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "게시글 없음", value = """
                            {
                              "message": "게시글이 존재하지 않습니다"
                            }
                            """)
            }))
    })
    @PostMapping
    ResponseEntity<PostLikeResponse> addLike(
            @Parameter(description = "게시글 ID", example = "3") @PathVariable Long postId
    );

    @Operation(
            summary = "게시글 좋아요 취소",
            description = "특정 게시글의 좋아요를 취소합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "좋아요 취소 완료"),
            @ApiResponse(responseCode = "404", description = "좋아요를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "좋아요 없음", value = """
                            {
                              "message": "좋아요를 찾을 수 없습니다"
                            }
                            """)
            }))
    })
    @DeleteMapping
    ResponseEntity<Void> removeLike(
            @Parameter(description = "게시글 ID", example = "3") @PathVariable Long postId
    );

    @Operation(
            summary = "게시글 좋아요 여부 확인",
            description = "특정 게시글에 대한 좋아요 여부를 확인합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 여부", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "좋아요 여부", value = """
                            {
                              "liked": true
                            }
                            """)
            }))
    })
    @GetMapping("/check")
    ResponseEntity<Boolean> checkLike(
            @Parameter(description = "게시글 ID", example = "3") @PathVariable Long postId
    );
} 