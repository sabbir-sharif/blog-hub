package com.blog_hub.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long id;

    private String title;

    private String content;

    private String status;

    private Long userId;

    private String authorName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
