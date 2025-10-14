package com.pikachu.backend.controller;

// 인증 API

import com.pikachu.backend.dto.AuthRequest;
import com.pikachu.backend.dto.AuthResponse;
import com.pikachu.backend.dto.RefreshTokenRequest;
import com.pikachu.backend.dto.RegisterRequest;
import com.pikachu.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 회원가입 API
    // POST /api/auth/register

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }


    // 로그인 API
    // POST /api/auth/login

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest request
    ) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }


    // 토큰 갱신 API
    // POST /api/auth/refresh

//    @PostMapping("/refresh")
//    public ResponseEntity<AuthResponse> refreshToken(
//            @Valid @RequestBody RefreshTokenRequest request
//    ) {
//        AuthResponse response = authService.refreshToken(request);
//        return ResponseEntity.ok(response);
//    }
}
