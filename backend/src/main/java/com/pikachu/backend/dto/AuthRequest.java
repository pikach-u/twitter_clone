package com.pikachu.backend.dto;

// 로그인 요청 DTO

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
