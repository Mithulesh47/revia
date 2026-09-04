package com.revia.identity.dto;

import com.revia.identity.Role;
import com.revia.identity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        Role role,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}