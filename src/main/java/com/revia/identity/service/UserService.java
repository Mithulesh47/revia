package com.revia.identity.service;

import com.revia.identity.Role;
import com.revia.identity.entity.User;
import com.revia.identity.UserStatus;
import com.revia.identity.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(
            String email,
            String password,
            Role role,
            UserStatus status) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();

        user.setEmail(email);

        // Never store the plaintext password.
        user.setPasswordHash(passwordEncoder.encode(password));

        user.setRole(role);
        user.setStatus(status);

        return userRepository.save(user);
    }
}