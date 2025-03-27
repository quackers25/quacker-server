package io.quacker.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserResetPasswordDto(
        @NotBlank
        @Email(regexp = "^(?=.*\\.).*$", message = "이메일 형식")
        String email,

        @Pattern(regexp = "(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\p{Nd})(?=.*[!@#$%^&*(),.?\":{}|<>])" +
                "[\\p{L}\\p{Nd}!@#$%^&*(),.?\":{}|<>]{8,16}",
                message = "비밀번호는 8~16자 사이여야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
        String newPassword,

        @Pattern(regexp = "(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\p{Nd})(?=.*[!@#$%^&*(),.?\":{}|<>])" +
                "[\\p{L}\\p{Nd}!@#$%^&*(),.?\":{}|<>]{8,16}",
                message = "비밀번호는 8~16자 사이여야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
        String newConfirmPassword,

        String code
) {}
