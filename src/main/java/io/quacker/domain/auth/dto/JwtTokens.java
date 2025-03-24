package io.quacker.domain.auth.dto;

import lombok.Builder;

@Builder
public record JwtTokens(
        String accessToken,
        String refreshToken
){}
