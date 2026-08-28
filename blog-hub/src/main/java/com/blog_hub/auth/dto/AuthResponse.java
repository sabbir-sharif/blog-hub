package com.blog_hub.auth.dto;

//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//
//@Data
//@Builder
//@AllArgsConstructor
//public class AuthResponse {
//
//    private String token;
//    private String message;
//}

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private String refreshToken;

//    private String message;
}