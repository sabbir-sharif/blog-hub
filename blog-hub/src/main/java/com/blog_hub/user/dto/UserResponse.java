package com.blog_hub.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String bio;

    private String profileImage;

    //private String role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}