package com.revia.identity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(
                request.email(),
                request.passwordHash(),
                request.role(),
                request.status()
        );
    }

    public record CreateUserRequest(
            String email,
            String passwordHash,
            String role,
            String status
    ) {
    }
}