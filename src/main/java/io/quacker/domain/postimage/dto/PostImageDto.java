package io.quacker.domain.postimage.dto;

import io.quacker.domain.postimage.entity.PostImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record PostImageDto(

        @Schema(description = "이미지 ID", example = "1")
        Long id,

        @Schema(description = "S3에 저장된 이미지 URL", example = "https://s3.amazonaws.com/your-bucket/image.jpg")
        String imageUrl

) {
    // 단일 PostImage 엔티티 → PostImageDto 변환
    public static PostImageDto fromEntity(PostImage postImage) {
        return PostImageDto.builder()
                .id(postImage.getId())
                .imageUrl(postImage.getImageUrl())
                .build();
    }

    // 리스트 변환 (List<PostImage> → List<PostImageDto>)
    public static List<PostImageDto> fromEntities(List<PostImage> postImages) {
        if (postImages == null) {
            return List.of(); // 빈 리스트 반환
        }
        return postImages.stream()
                .map(PostImageDto::fromEntity)
                .toList();
    }
}
