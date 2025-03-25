package io.quacker.domain.postlike.dto;

public record PostLikeResponse(
    boolean isLiked,
    int likeCount
) {} 