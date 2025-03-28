package io.quacker.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Schema(description = "사용자 로그인 Dto")
@Builder
public record UserLoginDto(

        @Schema(description = "사용자 이메일", example = "quacker2025@gamil.com")
        @NotBlank
        @Email(regexp = "^(?=.*\\.).*$", message = "이메일 형식")
        String email,

        @Schema(description = "사용자 비밀번호", example = "passworD!123")
        @Pattern(regexp = "(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\p{Nd})(?=.*[!@#$%^&*(),.?\":{}|<>])" +
                "[\\p{L}\\p{Nd}!@#$%^&*(),.?\":{}|<>]{8,16}",
                message = "비밀번호는 8~16자 사이여야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
        String password,

        @Schema(description = "자동로그인 여부", example = "true")
        boolean isAutoLogin
) {}
