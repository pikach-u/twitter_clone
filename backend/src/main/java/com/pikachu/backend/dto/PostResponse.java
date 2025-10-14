package com.pikachu.backend.dto;

import com.pikachu.backend.entity.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

// 응답 DTO
@Data
@Builder
public class PostResponse {
    private Long id;
    private String content;
    private UserResponse author;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity → DTO 변환
    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .author(UserResponse.from(post.getAuthor()))
//                .likeCount(post.getLikeCount())
//                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
