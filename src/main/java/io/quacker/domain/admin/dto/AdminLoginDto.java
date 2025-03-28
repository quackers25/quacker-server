package io.quacker.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "관리자 로그인 Dto")
@Builder
public record AdminLoginDto(
        @Schema(description = "관리자 이름(식별자)", example = "adminName")
        String username,
        @Schema(description = "관리자 비밀번호", example = "password")
        String password
){ }