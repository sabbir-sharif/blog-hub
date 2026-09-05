package com.blog_hub.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private Long id;
    private String content;

    private Long userId;
    private String userName;

    private Long postId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
