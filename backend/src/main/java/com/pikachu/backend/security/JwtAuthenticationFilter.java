package com.pikachu.backend.security;

// 모든 요청에서 JWT Token을 검증하는 필터

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 요청당 한 번만 실행되는 필터
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException{
        // 1. Authorization 헤더에서 JWT Token 추출
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {  // Bearer Token: Authorization 헤더의 표준 JWT 전송 방식
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);  // "Bearer " 제거

        // 2. Token에서 사용자 식별자 추출
        final String userIdentifier = jwtService.extractUsername(jwt);

        // 3. 사용자 인증 정보가 없으면 DB에서 조회
        if (userIdentifier != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(userIdentifier);

            // 4. Token 유효성 검증
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // 5. SecurityContext에 인증 정보 설정
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);    // SecurityContextHolder: Spring Security의 인증 정보 저장소
            }
        }

        filterChain.doFilter(request, response);
    }
}
