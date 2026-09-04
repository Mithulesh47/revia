package com.revia.identity;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String role,
        String status,
        Instant createdAt,
        Instant updatedAt
) {
}