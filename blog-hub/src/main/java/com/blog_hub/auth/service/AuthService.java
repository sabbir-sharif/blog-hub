package com.blog_hub.auth.service;

import com.blog_hub.auth.dto.AuthResponse;
import com.blog_hub.auth.dto.LoginRequest;
import com.blog_hub.auth.dto.RefreshTokenRequest;
import com.blog_hub.auth.dto.RegisterRequest;
import com.blog_hub.exception.DuplicateResourceException;
import com.blog_hub.exception.ResourceNotFoundException;
import com.blog_hub.refresh.entity.RefreshToken;
import com.blog_hub.refresh.service.RefreshTokenService;
import com.blog_hub.role.entity.Role;
import com.blog_hub.role.repository.RoleRepository;
import com.blog_hub.security.jwt.JwtService;
import com.blog_hub.security.service.CustomUserDetailsService;
import com.blog_hub.user.dto.UserResponse;
import com.blog_hub.user.entity.User;
import com.blog_hub.user.mapper.UserMapper;
import com.blog_hub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;

    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() ->
                        new ResourceNotFoundException("User role not found"));

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Encrypt password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setBio(request.getBio());
        user.setProfileImage(request.getProfileImage());
        user.setRole(userRole);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);

    }

    public AuthResponse login(LoginRequest request) {

//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Invalid email or password"));
//
//        boolean matches = passwordEncoder.matches(
//                request.getPassword(),
//                user.getPassword()
//        );
//
//        if (!matches) {
//            throw new ResourceNotFoundException("Invalid email or password");
//        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(
                        request.getEmail()
                );

        // JWT generate
        String token = jwtService.generateToken(userDetails);

        // refresh token generate
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
//                .message("Login Successful")
                .build();
        //return new AuthResponse("Login Successful");
    }

    public AuthResponse refreshAccessToken(
            RefreshTokenRequest request) {

        // Find refresh token in database
        RefreshToken refreshToken =
                refreshTokenService.findByToken(request.getRefreshToken());

        // Check expiration
        refreshTokenService.verifyExpiration(refreshToken);

        // Get associated user
        User user = refreshToken.getUser();

        // Load UserDetails
        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(user.getEmail());

        // Generate new access token
        String newToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(newToken)
                .refreshToken(refreshToken.getToken())
//                .message("Access token refreshed successfully")
                .build();
    }

    public void logout(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        refreshTokenService.deleteByUser(user);
    }
}
