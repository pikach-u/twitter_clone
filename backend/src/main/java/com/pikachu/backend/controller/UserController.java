package com.pikachu.backend.controller;

// 사용자 API

import com.pikachu.backend.dto.PostResponse;
import com.pikachu.backend.dto.UserResponse;
import com.pikachu.backend.service.PostService;
import com.pikachu.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;


    @GetMapping("/{userid}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userid) {
        UserResponse user = userService.getUserById(userid);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userid}/posts")
    public ResponseEntity<List<PostResponse>> getUserPosts(@PathVariable Long userid) {
        List<PostResponse> posts = postService.getPostsByUserId(userid);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{userid}/follow-status")
    public ResponseEntity<UserResponse> getFollowStatus(@PathVariable Long userid) {
        UserResponse response = userService.getFollowStatusForUser(userid);
        return ResponseEntity.ok(response);
    }
}
