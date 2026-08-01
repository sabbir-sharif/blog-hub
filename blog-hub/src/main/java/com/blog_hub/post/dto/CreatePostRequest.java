package com.blog_hub.post.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 10)
    private String title;

    @NotBlank(message = "Content is required")
    @Size(min = 50, max = 200)
    private String content;

    private String status;

    private Long userId;
}
