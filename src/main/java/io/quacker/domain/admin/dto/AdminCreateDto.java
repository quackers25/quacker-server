package io.quacker.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "관리자 생성 Dto")
@Builder
public record AdminCreateDto(
        @Schema(description = "관리자 이름(식별자)", example = "adminName")
        String username,
        @Schema(description = "관리자 비밀번호", example = "password")
        String password,
        @Schema(description = "관리자 확인비밀번호", example = "password")
        String confirmPassword,
        @Schema(description = "관리자 생성코드", example = "<<설정코드>>")
        String code
){ }