package com.revia.identity.auth;

public record LoginResponse(
        String accessToken,
        String tokenType
) {
}