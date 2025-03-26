package io.quacker.domain.comment.controller;

import io.quacker.domain.comment.dto.CommentDto;
import io.quacker.domain.comment.service.CommentService;
import io.quacker.domain.comment.dto.CommentCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 API", description = "게시글에 댓글을 등록, 조회, 수정, 삭제하는 API입니다.")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "특정 게시글에 댓글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 작성 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId,
            @RequestBody CommentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(postId, request.text()));
    }

    @Operation(
            summary = "댓글 수정",
            description = "특정 댓글의 내용을 수정합니다."
    )
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> update(
            @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId,
            @Parameter(description = "수정할 댓글 내용", example = "수정된 댓글입니다.")
            @RequestParam String text) {
        return ResponseEntity.ok(commentService.updateComment(commentId, text));
    }

    @Operation(
            summary = "댓글 삭제",
            description = "특정 댓글을 삭제합니다."
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "댓글 ID", example = "1") @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "게시글의 모든 댓글 조회",
            description = "특정 게시글에 작성된 모든 댓글을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<CommentDto>> getByPost(
            @Parameter(description = "게시글 ID", example = "1") @PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }
}