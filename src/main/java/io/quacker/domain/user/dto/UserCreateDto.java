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
        boolean isPrivate
) {}
