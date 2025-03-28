package io.quacker.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@Schema(name="이메일 인증코드 Dto", description = "이메일 인증코드를 전달합니다.")
public record UserEmailCodeDto(
        @Schema(description = "이메일", example = "quacker2025@gmail.com")
        @NotBlank
        @Email(regexp = "^(?=.*\\.).*$", message = "이메일 형식")
        String email,
        @Schema(description = "인증코드", example = "<<UUID>>")
        String code
) {}
