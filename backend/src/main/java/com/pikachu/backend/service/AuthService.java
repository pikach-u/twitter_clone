package com.pikachu.backend.service;

import com.pikachu.backend.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;     // ms 단위. 예: 24시간 = 86400000ms

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration; // ms 단위

//    JWT 토큰 생성
//    @param userDetail 사용자 정보
//    @return JWT 토큰 문자열

    public String generateToken(UserDetails userDetails){
        Map<String, Object> extraClaims = new HashMap<>();

        if(userDetails instanceof User user){   // 사용자 정보를 토큰에 포함
            extraClaims.put("id", user.getId());
        }
    }

}
