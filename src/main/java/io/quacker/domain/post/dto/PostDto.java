package io.quacker.domain.post.dto;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.dto.UserDto;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record PostDto(
        Long id,
        String text,
        int likeCount,
        int repostCount,
        UserDto user,
        PostDto originPost // 리트윗, 공유된 원본 글
//        List<CommentDto> comments,
//        List<PostImageDto> postImages,
//        List<PostMentionDto> postMentions,
//        List<HashtagPostDto> hashtagPosts,
//        List<PostLikeDto> likes
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
//                .comments(post.getComments().stream().map(CommentDto::from).toList())
//                .postImages(post.getPostImages().stream().map(PostImageDto::from).toList())
//                .postMentions(post.getPostMentions().stream().map(PostMentionDto::from).toList())
//                .hashtagPosts(post.getHashtagPosts().stream().map(HashtagPostDto::from).toList())
//                .likes(post.getLikes().stream().map(PostLikeDto::from).toList())
                .build();
    }

    // List<Post>를 List<PostDto>로 변환하는 정적 팩토리 메서드
    public static List<PostDto> of(List<Post> posts) {
        return posts.stream().map(PostDto::from).collect(Collectors.toList());
    }
}