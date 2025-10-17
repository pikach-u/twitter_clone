package com.pikachu.backend.service;

import com.pikachu.backend.entity.Like;
import com.pikachu.backend.entity.Post;
import com.pikachu.backend.entity.User;
import com.pikachu.backend.exception.ResourceNotFoundException;
import com.pikachu.backend.repository.LikeRepository;
import com.pikachu.backend.repository.PostRepository;
import com.pikachu.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 좋아요 토글 (누르면 좋아요, 다시 누르면 취소)
     */
    public boolean toggleLike(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다"));

        // 이미 좋아요했는지 확인
        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            // 좋아요 취소
            likeRepository.delete(existingLike.get());
            return false;  // 취소됨
        } else {
            // 좋아요 추가
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            return true;  // 추가됨
        }
    }

    /**
     * 특정 게시물에 좋아요 했는지 확인
     */
    @Transactional(readOnly = true)
    public boolean isLiked(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시물을 찾을 수 없습니다"));

        return likeRepository.existsByUserAndPost(user, post);
    }
}
