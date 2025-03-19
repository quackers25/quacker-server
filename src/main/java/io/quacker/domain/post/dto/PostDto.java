package io.quacker.domain.post.dto;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.postimage.dto.PostImageDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record PostDto(
        Long id,
        String text,
        int likeCount,
        int repostCount,
        UserDto user,
        PostDto originPost, // 리트윗, 공유된 원본 글
        List<PostDto> reposts,
        List<CommentDto> comments,
        List<PostImageDto> postImages,
        List<PostMentionDto> postMentions,
        List<HashtagPostDto> hashtagPosts,
        List<PostLikeDto> likes
) {
    // Post 엔티티를 PostDto로 변환하는 정적 팩토리 메서드
    public static PostDto from(Post post) {
        return new PostDto(
                post.getId(),
                post.getText(),
                post.getLikeCount(),
                post.getRepostCount(),
                new UserDto(post.getUser().getId(), post.getUser().getName()),
                Optional.ofNullable(post.getOriginPost()).map(PostDto::from).orElse(null), // 리트윗 처리
                post.getReposts().stream().map(PostDto::from).collect(Collectors.toList()), // 답글 목록
                post.getComments().stream().map(comment -> new CommentDto(comment.getId(), comment.getText())).collect(Collectors.toList()),
                post.getPostImages().stream().map(img -> new PostImageDto(img.getId(), img.getImageUrl())).collect(Collectors.toList()),
                post.getPostMentions().stream().map(mention -> new PostMentionDto(mention.getId(), mention.getMentionedUser().getId())).collect(Collectors.toList()),
                post.getHashtagPosts().stream().map(tag -> new HashtagPostDto(tag.getId(), tag.getHashtag().getTag())).collect(Collectors.toList()),
                post.getLikes().stream().map(like -> new PostLikeDto(like.getId(), like.getUser().getId())).collect(Collectors.toList())
        );
    }

    // List<Post>를 List<PostDto>로 변환하는 정적 팩토리 메서드
    public static List<PostDto> of(List<Post> posts) {
        return posts.stream().map(PostDto::from).collect(Collectors.toList());
    }
}