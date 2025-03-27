package io.quacker.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentCreateRequest(
        @Schema(description = "댓글 내용", example = "좋은 글 감사합니다!")
        String text
) {}