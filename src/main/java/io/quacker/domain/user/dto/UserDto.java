package io.quacker.domain.user.dto;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(

        String email,

        String name,

        String bio,

        String avatarImageUrl,

        boolean isVerified,

        boolean isLocked, // 정지

        boolean isPrivate, // 계정 공개

        List<Post> posts
){
    public static UserDto from(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .bio(user.getBio())
                .avatarImageUrl(user.getAvatarImageUrl())
                .isVerified(user.isVerified())
                .isLocked(user.isLocked())
                .isPrivate(user.isPrivate())
                .build();
    }
}
