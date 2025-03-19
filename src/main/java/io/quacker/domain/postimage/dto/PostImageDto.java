package io.quacker.domain.postimage.dto;

import io.quacker.domain.postimage.entity.PostImage;

import java.util.List;
import java.util.stream.Collectors;

public record PostImageDto(
        Long id,
        String imageUrl
) {
    // 단일 PostImage 엔티티 → PostImageDto 변환
    public static PostImageDto fromEntity(PostImage postImage) {
        return new PostImageDto(postImage.getId(), postImage.getImageUrl());
    }

    // 리스트 변환 (List<PostImage> → List<PostImageDto>)
    public static List<PostImageDto> fromEntities(List<PostImage> postImages) {
        return postImages.stream().map(PostImageDto::fromEntity).collect(Collectors.toList());
    }
}
