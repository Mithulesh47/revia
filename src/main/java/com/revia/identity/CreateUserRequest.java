package com.revia.identity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        String passwordHash,

        @NotBlank
        String role,

        @NotBlank
        String status

) {
}
