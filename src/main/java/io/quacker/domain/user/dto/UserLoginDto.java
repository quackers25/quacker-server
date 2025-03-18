package io.quacker.domain.user.dto;

public record UserLoginDto(
        String email,
        String password
) {}
