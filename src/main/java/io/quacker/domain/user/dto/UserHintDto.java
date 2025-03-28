package io.quacker.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@Schema(name = "힌트 Dto", description = "힌트를 전달합니다.")
public record UserHintDto(
        @Schema(description = "힌트 문자열", example = "hint")
        String hint
) {}
