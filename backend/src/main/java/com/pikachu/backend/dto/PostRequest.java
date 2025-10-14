package com.pikachu.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

// 요청 DTO
@Data
public class PostRequest {
    @NotBlank(message = "게시물 내용은 필수입니다")
    @Size(max = 280, message = "게시물은 280자를 초과할 수 없습니다")
    private String content;
}

