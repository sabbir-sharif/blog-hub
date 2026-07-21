package com.blog_hub.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String bio;

    private String profileImage;

    private String role;

    private LocalDateTime createdAt;
}