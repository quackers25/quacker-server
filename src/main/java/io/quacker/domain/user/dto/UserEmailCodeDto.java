package io.quacker.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserEmailCodeDto(
        @NotBlank
        @Email(regexp = "^(?=.*\\.).*$", message = "이메일 형식")
        String email,
        String code
) {}
