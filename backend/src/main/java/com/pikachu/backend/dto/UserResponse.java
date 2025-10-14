package com.pikachu.backend.dto;

// 사용자 응답 DTO

import com.pikachu.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    // private String fullName;
    private String profileImageUrl;
    private String bio;
    private Long followersCount;
    private Long followingCount;
    private boolean isFollowing;

    private String accessToken;
    private String refreshToken;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .followersCount(user.getFollowersCount())
                .followingCount(user.getFollowingsCount())
                .build();
    }

}
