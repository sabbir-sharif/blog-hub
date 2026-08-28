package com.blog_hub.refresh.service;

import com.blog_hub.exception.ResourceNotFoundException;
import com.blog_hub.refresh.entity.RefreshToken;
import com.blog_hub.refresh.repository.RefreshTokenRepository;
import com.blog_hub.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // 7 days
    private final long refreshTokenDuration =
            7L * 24 * 60 * 60 * 1000;


    public RefreshToken createRefreshToken(User user) {

        // Check whether this user already has a refresh token
        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByUser(user)
                        .orElse(null);

        if (refreshToken == null) {

            // First login
            refreshToken = new RefreshToken();

            refreshToken.setUser(user);

        }

        // Generate a new refresh token
        refreshToken.setToken(
                UUID.randomUUID().toString()
        );

        // Reset expiration
        refreshToken.setExpiryDate(
                Instant.now()
                        .plusMillis(refreshTokenDuration)
        );

        return refreshTokenRepository.save(refreshToken);
    }


    public RefreshToken findByToken(String token) {

        return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Refresh token not found"
                        )
                );
    }


    public RefreshToken verifyExpiration(
            RefreshToken refreshToken) {

        if (refreshToken.getExpiryDate()
                .isBefore(Instant.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new RuntimeException(
                    "Refresh token has expired"
            );
        }

        return refreshToken;
    }


    public void deleteByUser(User user) {

        refreshTokenRepository.deleteByUser(user);
    }
}