package com.blog_hub.auth.service;

import com.blog_hub.auth.dto.AuthResponse;
import com.blog_hub.auth.dto.LoginRequest;
import com.blog_hub.auth.dto.RegisterRequest;
import com.blog_hub.exception.DuplicateResourceException;
import com.blog_hub.exception.ResourceNotFoundException;
import com.blog_hub.user.dto.UserResponse;
import com.blog_hub.user.entity.User;
import com.blog_hub.user.mapper.UserMapper;
import com.blog_hub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Encrypt password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setBio(request.getBio());
        user.setProfileImage(request.getProfileImage());

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid email or password"));

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!matches) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        return new AuthResponse("Login Successful");

    }
}
