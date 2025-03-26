package io.quacker.domain.comment.dto;

import io.quacker.domain.comment.entity.Comment;
import lombok.Builder;

@Builder
public record CommentDto(
        Long id,
        String text,
        Long postId,
        Long userId,
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