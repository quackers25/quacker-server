package io.quacker.domain.user.dto;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
public record UserUpdateDto(

        String nickname,

        String email,

        String name,

        String bio,

        String avatarImageUrl,

        boolean isVerified,

        boolean isLocked, // 정지

        boolean isPrivate, // 계정 공개

        List<Post> posts
){

}
