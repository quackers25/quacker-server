package io.quacker.domain.comment.dto;

import io.quacker.domain.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CommentDto(

        @Schema(description = "댓글 ID", example = "1")
        Long id,

        @Schema(description = "댓글 내용", example = "이 게시글 정말 재미있네요!")
        String text,

        @Schema(description = "게시글 ID", example = "10")
        Long postId,

        @Schema(description = "작성자 ID", example = "5")
        Long userId,

        @Schema(description = "작성자 이름", example = "quacker123")
        String userName

) {
    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getName())
                .build();
    }
}