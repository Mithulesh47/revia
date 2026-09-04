package com.revia.identity.controller;

import com.revia.identity.entity.User;
import com.revia.identity.dto.CreateUserRequest;
import com.revia.identity.dto.UserResponse;
import com.revia.identity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(
            @Valid @RequestBody CreateUserRequest request) {

        User user = userService.createUser(
                request.email(),
                request.password(),
                request.role(),
                request.status()
        );

        return toResponse(user);
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser(
            Authentication authentication) {

        UUID userId = (UUID) authentication.getPrincipal();

        User user = userService.getUserById(userId);

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}