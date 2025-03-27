package io.quacker.domain.admin.dto;

import lombok.Builder;

@Builder
public record AdminLoginDto(
        String username,
        String password
){ }