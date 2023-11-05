package com.meowzip.apiserver.jwt.dto.response;

public record JwtResponseDTO(
        String accessToken,
        String refreshToken
) {}
