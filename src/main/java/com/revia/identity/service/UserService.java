package com.revia.identity.service;

import com.revia.identity.Role;
import com.revia.identity.entity.User;
import com.revia.identity.repository.UserRepository;
import com.revia.identity.UserStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(
            String email,
            String passwordHash,
            Role role,
            UserStatus status) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setRole(role);
        user.setStatus(status);

        return userRepository.save(user);
    }
}