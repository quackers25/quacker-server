package io.quacker.domain.user.dto;

import io.quacker.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UserCreateDto (
        String email,
        String password,
        String confirmPassword,
        String name,
        String bio,
        String avatarImageUrl,
        boolean verified,
        boolean isPrivate
) {

    //Todo : Entity
    public User toUserWtihHashedPassword(String hashedPw) {
        return User.builder()
                .email(email)
                .password(hashedPw)
                .name(name)
                .bio(bio)
                .avatarImageUrl(avatarImageUrl)
                .verified(verified)
                .isPrivate(isPrivate)
                .build();
    }
}
