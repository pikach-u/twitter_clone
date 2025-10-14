package com.pikachu.backend.service;

import com.pikachu.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
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

        // User 정보를 Token에 포함
        // userDetails가 User Entity이면, 정보를 extraClaims에 넣는다
        // instanceof : Java 패턴 매칭
        if(userDetails instanceof User user){   // 사용자 정보를 토큰에 포함
            extraClaims.put("id", user.getId());
            extraClaims.put("email", user.getEmail());
            extraClaims.put("username", user.getUsername());

        }

        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // Token 생성 로직. 실제 JWT를 만든다
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims) // payload에 extraClaims를 넣음 (id, email 등)
                .setSubject(userDetails.getUsername())  // 사용자 식별
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 만료 시간 설정
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // 서명. getSignInKey() 는 디코딩 후 Key를 만들어서 반환
                .compact();
    }

    // Token에서 사용자명 추출 (id)
    public String extractUsername(String token){
        Claims claims = extractAllClaims(token);

        // Token에 id가 있으면 id를 반환 (OAuth 사용자)
        if(claims.containsKey("id")){
            return String.valueOf(claims.get("id"));
        }
        return claims.getSubject(); // payload에 id가 없으면 username 리턴
    }

    // Token 유효성 검증
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String identifier = extractUsername(token);

        if(userDetails instanceof User user){
            // id 또는 username으로 매칭
            // Token에 들어있는 식별자와 일치하는지 확인
            boolean isVaild = identifier.equals(String.valueOf(user.getId()))
                    || identifier.equals(user.getUsername());
            return isVaild && !isTokenExpired(token);
        }

        return identifier.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)  // Token의 Signature를 검증, payload(claims)를 돌려준다. 검증되지 않았을 경우 예외처리 필요
                .getBody();
    }

    private Key getSignInKey(){
        // secretKey(Base64) -> HMAC-SHA. BASE64가 아닌 원문을 넣지 않게 주의
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
