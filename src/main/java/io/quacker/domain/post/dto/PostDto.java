package io.quacker.domain.post.dto;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postimage.dto.PostImageDto;
import io.quacker.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record PostDto(
        @Schema(description = "게시글 ID", example = "1")
        Long id,

        @Schema(description = "게시글 본문 내용", example = "오늘도 개발 중")
        String text,

        @Schema(description = "좋아요 수", example = "15")
        int likeCount,

        @Schema(description = "리트윗 수", example = "3")
        int repostCount,

        @Schema(description = "작성자 정보")
        UserDto user,

        @Schema(description = "리트윗의 원본 게시글 정보")
        PostDto originPost,

        @Schema(description = "게시글에 포함된 이미지 목록")
        List<PostImageDto> images
) {
    // Post 엔티티를 PostDto로 변환하는 정적 팩토리 메서드
    public static PostDto from(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .text(post.getText())
                .likeCount(post.getLikeCount())
                .repostCount(post.getRepostCount())
                .user(UserDto.from(post.getUser()))
                .originPost(post.getOriginPost() != null ? PostDto.from(post.getOriginPost()) : null)
                .images(PostImageDto.fromEntities(post.getPostImages()))
                .build();
    }

    // List<Post>를 List<PostDto>로 변환하는 정적 팩토리 메서드
    public static List<PostDto> of(List<Post> posts) {
        return posts.stream().map(PostDto::from).toList();
    }

    public static PostDto empty() {
        return PostDto.builder()
                .text("")
                .likeCount(0)
                .repostCount(0)
                .images(List.of()) // 이미지 리스트도 비워두기
                .build();
    }
}