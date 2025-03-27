package io.quacker.domain.auth.dto;

import lombok.Builder;

@Builder
public record JwtTokens(
        Long userId,
        String accessToken,
        String refreshToken
){}
