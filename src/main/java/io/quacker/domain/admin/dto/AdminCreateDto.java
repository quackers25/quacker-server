package io.quacker.domain.admin.dto;

import lombok.Builder;

@Builder
public record AdminCreateDto(
        String username,
        String password,
        String confirmPassword,
        String code
){ }