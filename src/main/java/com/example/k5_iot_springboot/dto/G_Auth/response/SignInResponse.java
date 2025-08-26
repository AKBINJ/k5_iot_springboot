package com.example.k5_iot_springboot.dto.G_Auth.response;

import java.util.Set;

public record SignInResponse(
        String tokenType,       // "Bearer"
        String accessToken,     // JWT
        long expiresAt,         // 만료시각 (ms)
        String username,        // loginId
        Set<String> roles       // ["ROLE_USER"]
) {}