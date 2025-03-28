package io.quacker.domain.user.dto;

import io.quacker.domain.post.entity.Post;
import io.quacker.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "사용자 수정 ")
@Builder
public record UserUpdateDto(

        @Schema(description = "사용자 새 이름", example = "newOne")
        String name,

        @Schema(description = "사용자 새 자기소개", example = "Bye")
        String bio,

        @Schema(description = "사용자 새 프로필 ", example = "https://img.icons8.com/color-pixels/32/duck.png")
        String avatarImageUrl,

        @Schema(description = "사용자 새 잠금상태", example = "true")
        boolean isLocked, // 정지

        @Schema(description = "사용자 새 공개여부", example = "true")
        boolean isPrivate// 계정 공개
){

}
