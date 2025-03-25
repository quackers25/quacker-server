package io.quacker.domain.userfollowing.dto;

import io.quacker.domain.user.entity.User;

public record FollowResponseDto(Long userId, String name, String bio, String profileImageUrl) {

    public static FollowResponseDto from(User user) {
        return new FollowResponseDto(user.getId(), user.getName(), user.getBio(), user.getAvatarImageUrl());
    }

}
