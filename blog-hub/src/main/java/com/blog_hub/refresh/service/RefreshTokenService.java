package com.blog_hub.refresh.service;

//package com.blog_hub.auth.refresh.service;

import com.blog_hub.refresh.entity.RefreshToken;
import com.blog_hub.refresh.repository.RefreshTokenRepository;
import com.blog_hub.exception.ResourceNotFoundException;
import com.blog_hub.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final long refreshTokenDuration =
            7L * 24 * 60 * 60 * 1000;


    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token(UUID.randomUUID().toString())
                        .expiryDate(
                                Instant.now().plusMillis(
                                        refreshTokenDuration
                                )
                        )
                        .user(user)
                        .build();

        return refreshTokenRepository.save(refreshToken);
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


    public RefreshToken findByToken(String token) {

        return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Refresh token not found"
                        )
                );
    }
}
