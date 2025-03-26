package io.quacker.domain.userfollowing.dto;

import jakarta.validation.constraints.NotNull;

public record FollowRequestDto(@NotNull Long followingUserId) {

}
