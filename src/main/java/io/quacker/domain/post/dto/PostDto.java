package io.quacker.domain.post.dto;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.dto.UserDto;
import lombok.Builder;

import java.util.List;

@Builder
public record PostDto(
        Long id,
        String text,
        int likeCount,
        int repostCount,
        UserDto user,
        PostDto originPost // 리트윗, 공유된 원본 글
//        List<PostImageDto> images
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
//               .images(PostImageDto.fromEntities(post.getPostImages()))
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
//                .images(List.of()) // 이미지 리스트도 비워두기
                .build();
    }
}