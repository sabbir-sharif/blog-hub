package com.blog_hub.refresh.repository;

//package com.blog_hub.auth.refresh.repository;

import com.blog_hub.refresh.entity.RefreshToken;
import com.blog_hub.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);
}
