package com.blog_hub.user.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @Size(min = 3, max = 50)
    private String name;

    @Size(max = 300)
    private String bio;

    private String profileImage;
}