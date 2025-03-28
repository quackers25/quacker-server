package io.quacker.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Schema(description = "사용자 회원가입 Dto")
@Builder
public record UserCreateDto (

        @Schema(description = "사용자 이메일", example = "quacker2025@gamil.com")
        @NotBlank
        @Email(regexp = "^(?=.*\\.).*$")
        String email,

        @Schema(description = "사용자 비밀번호", example = "passworD!123")
        @Pattern(regexp = "(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\p{Nd})(?=.*[!@#$%^&*(),.?\":{}|<>])" +
                "[\\p{L}\\p{Nd}!@#$%^&*(),.?\":{}|<>]{8,16}",
                message = "비밀번호는 8~16자 사이여야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
        String password,

        @Schema(description = "사용자 확인비밀번호", example = "passworD!123")
        @Pattern(regexp = "(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\p{Nd})(?=.*[!@#$%^&*(),.?\":{}|<>])" +
                "[\\p{L}\\p{Nd}!@#$%^&*(),.?\":{}|<>]{8,16}",
                message = "비밀번호는 8~16자 사이여야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
        String confirmPassword,

        @Schema(description = "사용자 이름", example = "Username")
        @Pattern(regexp = "^[\\p{L}0-9]{2,16}$")
        String name,

        @Schema(description = "사용자 자기소개", example = "Hello world!")
        String bio,

        @Schema(description = "사용자 프로필사진", example = "https://img.icons8.com/color-pixels/32/duck.png")
        //@Pattern(regexp = "")
        String avatarImageUrl,

        @Schema(description = "사용자 이메일 찾기 힌트", example = "hint")
        String hint,

        @Schema(description = "사용자 비공개 여부", example = "false")
        boolean isPrivate

) {}
