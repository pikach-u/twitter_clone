package com.pikachu.backend.controller;

import com.pikachu.backend.dto.FollowResponse;
import com.pikachu.backend.dto.UserResponse;
import com.pikachu.backend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{targetUserId}/follow")
    public ResponseEntity<Boolean> toggleFollow(@PathVariable Long targetUserId, Authentication auth) {
        String currentUsername = auth.getName();
        boolean response = followService.toggleFollow(targetUserId, currentUsername);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable Long userId) {
        List<UserResponse> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserResponse>> getFollowing(@PathVariable Long userId) {
        List<UserResponse> following = followService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }
}