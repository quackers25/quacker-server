package io.quacker.domain.postlike.dto;

public record PostLikeResponseDto(
    boolean isLiked,
    int likeCount
) {
} 