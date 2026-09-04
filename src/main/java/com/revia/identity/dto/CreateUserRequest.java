package com.revia.identity.dto;

import com.revia.identity.Role;
import com.revia.identity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        String passwordHash,

        @NotNull
        Role role,

        @NotNull
        UserStatus status

) {
}