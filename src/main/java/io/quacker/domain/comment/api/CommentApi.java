package io.quacker.domain.comment.api;

import io.quacker.domain.comment.dto.CommentCreateRequest;
import io.quacker.domain.comment.dto.CommentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글 API", description = "게시글에 댓글을 등록, 조회, 수정, 삭제하는 API입니다.")
@RequestMapping("/api/v1/posts/{postId}/comments")
public interface CommentApi {

    @Operation(
            summary = "댓글 작성",
            description = "특정 게시글에 댓글을 작성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 작성 완료", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "작성 성공", value = """
                            {
                              "id": 1,
                              "text": "댓글 내용",
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
    ResponseEntity<CommentDto> addComment(
            @Parameter(description = "게시글 ID", example = "3") @PathVariable Long postId,
            @RequestBody(description = "댓글 작성 요청", required = true,
                    content = @Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CommentCreateRequest.class)))
            CommentCreateRequest request
    );

    @Operation(
            summary = "댓글 수정",
            description = "특정 댓글의 내용을 수정합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "수정 성공 예시", value = """
                        {
                          "id": 1,
                          "text": "수정된 댓글입니다.",
                          "postId": 3,
                          "userId": 2
                        }
                        """)
            })),
            @ApiResponse(responseCode = "404", description = "댓글 없음", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "댓글 없음 예시", value = """
                        {
                          "message": "댓글을 찾을 수 없습니다."
                        }
                        """)
            }))
    })
    @PutMapping("/{commentId}")
    ResponseEntity<CommentDto> updateComment(
            @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId,
            @Parameter(description = "수정할 댓글 내용", example = "수정된 댓글입니다.")
            @RequestParam String text
    );

    @Operation(
            summary = "댓글 삭제",
            description = "특정 댓글을 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "댓글 없음", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "댓글 없음 예시", value = """
                        {
                          "message": "댓글을 찾을 수 없습니다."
                        }
                        """)
            }))
    })
    @DeleteMapping("/{commentId}")
    ResponseEntity<Void> deleteComment(
            @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId
    );

    @Operation(summary = "게시글의 모든 댓글 조회", description = "특정 게시글에 작성된 모든 댓글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 목록", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "댓글 리스트", value = """
                    [
                      {
                        "id": 1,
                        "text": "첫 번째 댓글",
                        "postId": 3,
                        "userId": 2
                      },
                      {
                        "id": 2,
                        "text": "두 번째 댓글",
                        "postId": 3,
                        "userId": 4
                      }
                    ]
                    """)
    }))
    @GetMapping
    ResponseEntity<List<CommentDto>> getByPost(
            @Parameter(description = "게시글 ID", example = "3") @PathVariable Long postId
    );
}
